package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.NoOpUpdateSystem;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.GameMap;
import dev.prozilla.pine.examples.sokoban.component.History;
import dev.prozilla.pine.examples.sokoban.component.Move;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;
import dev.prozilla.pine.examples.sokoban.entity.CratePrefab;
import dev.prozilla.pine.examples.sokoban.entity.PlayerPrefab;
import dev.prozilla.pine.examples.sokoban.packet.*;
import dev.prozilla.pine.examples.sokoban.request.MoveRequest;
import dev.prozilla.pine.examples.sokoban.request.RestartRequest;
import dev.prozilla.pine.examples.sokoban.request.UndoRequest;
import dev.prozilla.pine.extensions.pinet.component.NetworkIdentity;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageHandler;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.response.ServerResponse;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

import java.util.*;

public class MessageHandler extends NoOpUpdateSystem implements ServerMessageHandler {
	
	private final NetworkManager network;
	private final GridGroup foregroundGrid;
	private final List<Vector2i> crateSpawns;
	
	public MessageHandler(NetworkManager network, GridGroup foregroundGrid) {
		super(NetworkIdentity.class, PlayerData.class, TileRenderer.class, SpriteRenderer.class, AudioEffectPlayer.class, History.class);
		setRequiredTag(EntityTag.PLAYER);
		this.network = network;
		this.foregroundGrid = foregroundGrid;
		
		crateSpawns = new ArrayList<>();
		for (Vector2i coordinate : foregroundGrid.coordinateToTile.keySet()) {
			TileRenderer tile = foregroundGrid.getTile(coordinate);
			if (tile != null && tile.getEntity().hasTag(EntityTag.CRATE)) {
				crateSpawns.add(tile.getCoordinate().clone());
			}
		}
	}
	
	@Override
	public void handleRequest(ServerRequest request) {
		switch (request.getPayload()) {
			case MoveRequest(Direction direction) -> move(request, direction);
			case UndoRequest ignored -> undo(request);
			case RestartRequest ignored -> restart(request);
			default -> {}
		}
	}
	
	@Override
	public void handleJoin(ServerRequest request) {
		Vector2i spawn = findSpawn();
		foregroundGrid.addTile(new PlayerPrefab(request.getSenderId()), spawn.x, spawn.y);
		
		request.reply(new WelcomePacket(request.getSenderId()));
		request.reply(createSnapshot());
		request.replyToOthers(new PlayerJoinPacket(request.getSenderId(), spawn));
	}
	
	@Override
	public void handleLeave(ServerRequest request) {
		EntityChunk chunk = getPlayer(request.getSenderId());
		if (chunk != null) {
			chunk.getEntity().destroy();
		}
		
		request.replyToOthers(new PlayerLeavePacket(request.getSenderId()));
	}
	
	@Override
	public void handleResponse(ServerResponse response) {
		switch (response.getPayload()) {
			case WelcomePacket(int playerId) -> {
				response.acknowledge();
				network.setLocalClientId(playerId);
			}
			case GameStatePacket state -> {
				response.acknowledge();
				applyGameState(state);
			}
			case PlayerJoinPacket(int playerId, Vector2i spawn) -> {
				response.acknowledge();
				applyPlayer(playerId, spawn);
			}
			case PlayerLeavePacket(int playerId) -> {
				response.acknowledge();
				despawnPlayer(playerId);
			}
			case PlayerMovePacket move -> {
				response.acknowledge();
				applyMove(move);
			}
			case RejectionPacket ignored -> {
				response.acknowledge();
				rejectPendingMove(network.getLocalClientId());
			}
			default -> {}
		}
	}
	
	@Override
	public void handleDisconnect(ServerResponse response) {
		response.acknowledge();
		GameManager.instance.leaveSession();
	}
	
	private void applyGameState(GameStatePacket state) {
		clearAwaitingConfirm(network.getLocalClientId());
		
		Map<Integer, Vector2i> playerPositions = new HashMap<>();
		for (Vector3i player : state.players()) {
			playerPositions.put(player.x, new Vector2i(player.y, player.z));
		}
		
		List<Vector2i> cratePositions = new ArrayList<>();
		for (Vector2i crate : state.crates()) {
			cratePositions.add(crate.clone());
		}
		
		applyState(playerPositions, cratePositions);
	}
	
	private void applyMove(PlayerMovePacket packet) {
		Move move = packet.move();
		
		int localId = network.getLocalClientId();
		if (move.playerId() == localId) {
			clearAwaitingConfirm(localId);
		}
		
		EntityChunk chunk = getPlayer(move.playerId());
		if (chunk == null) {
			return;
		}
		
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		TileRenderer tileRenderer = chunk.getComponent(TileRenderer.class);
		
		Vector2i coordinate = tileRenderer.getCoordinate();
		if (coordinate.equals(move.end())) {
			return;
		}
		
		if (move.playerId() == localId && playerData.timeUntilMoveCompletes > 0) {
			return;
		}
		
		playerData.beginMove(move, foregroundGrid);
	}
	
	private void rejectPendingMove(int playerId) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk == null) {
			return;
		}
		
		chunk.getComponent(History.class).revertPending(foregroundGrid);
		clearAwaitingConfirm(playerId);
	}
	
	private void despawnPlayer(int playerId) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk != null) {
			chunk.getEntity().destroy();
		}
	}
	
	private void clearAwaitingConfirm(int playerId) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk != null) {
			chunk.getComponent(PlayerData.class).awaitingConfirm = false;
		}
	}
	
	public void applyState(Map<Integer, Vector2i> playerCoordinates, List<Vector2i> crateCoordinates) {
		forEach(chunk -> {
			int id = chunk.getComponent(NetworkIdentity.class).id;
			
			if (!playerCoordinates.containsKey(id)) {
				chunk.getEntity().destroy();
				return;
			}
			
			foregroundGrid.removeTile(chunk.getComponent(TileRenderer.class));
		});
		
		clearCrates();
		for (Vector2i cratePosition : crateCoordinates) {
			foregroundGrid.addTile(new CratePrefab(), cratePosition.x, cratePosition.y);
		}
		
		for (Map.Entry<Integer, Vector2i> entry : playerCoordinates.entrySet()) {
			applyPlayer(entry.getKey(), entry.getValue());
		}
	}
	
	public void applyPlayer(int playerId, Vector2i coordinate) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk != null) {
			PlayerData playerData = chunk.getComponent(PlayerData.class);
			playerData.teleportTo(foregroundGrid, coordinate, playerData.direction);
			return;
		}
		
		if (!foregroundGrid.hasTile(coordinate)) {
			foregroundGrid.addTile(new PlayerPrefab(playerId), coordinate);
		}
	}
	
	private void move(ServerRequest request, Direction direction) {
		EntityChunk chunk = getPlayer(request.getSenderId());
		if (chunk == null) {
			reject(request);
			return;
		}
		
		History history = chunk.getComponent(History.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (!playerData.awaitingConfirm) {
			playerData.finishMove();
		}
		
		Move move = playerData.computeMove(foregroundGrid, direction);
		if (move == null) {
			reject(request);
			return;
		}
		
		history.push(move);
		if (!playerData.awaitingConfirm) {
			playerData.beginMove(move, foregroundGrid);
		}
		request.replyToAll(new PlayerMovePacket(move));
	}
	
	private void undo(ServerRequest request) {
		EntityChunk chunk = getPlayer(request.getSenderId());
		if (chunk == null) {
			reject(request);
			return;
		}
		
		chunk.getComponent(PlayerData.class).finishMove();
		
		if (chunk.getComponent(History.class).undo(foregroundGrid) != null) {
			request.replyToAll(createSnapshot());
		} else {
			reject(request);
		}
	}
	
	private void restart(ServerRequest request) {
		boolean allowed = request.isSentByHost();
		if (!allowed) {
			reject(request);
			return;
		}
		
		Map<Integer, Vector2i> playerPositions = new HashMap<>();
		Set<Vector2i> reserved = new HashSet<>(crateSpawns);
		forEach(chunk -> {
			int id = chunk.getComponent(NetworkIdentity.class).id;
			Vector2i spawn = findSpawn(reserved, false);
			reserved.add(spawn);
			
			playerPositions.put(id, spawn);
		});
		
		applyState(playerPositions, crateSpawns);
		
		forEach(chunk -> chunk.getComponent(History.class).clear());
		
		request.replyToAll(createSnapshot());
	}
	
	private void reject(ServerRequest request) {
		request.reply(new RejectionPacket());
		request.reply(createSnapshot());
	}
	
	private void clearCrates() {
		List<Entity> crates = new ArrayList<>();
		for (Vector2i coordinate : new ArrayList<>(foregroundGrid.coordinateToTile.keySet())) {
			TileRenderer tile = foregroundGrid.getTile(coordinate);
			if (tile != null && tile.getEntity().hasTag(EntityTag.CRATE)) {
				crates.add(tile.getEntity());
			}
		}
		for (Entity crate : crates) {
			crate.destroy();
		}
	}
	
	private EntityChunk getPlayer(int clientId) {
		EntityChunk result = null;
		
		if (hasEntityChunks()) {
			for (EntityChunk chunk : getChunks()) {
				if (chunk.getComponent(NetworkIdentity.class).id == clientId) {
					result = chunk;
				}
			}
		}
		
		return result;
	}
	
	private Vector2i findSpawn() {
		return findSpawn(new HashSet<>(), true);
	}
	
	private Vector2i findSpawn(Set<Vector2i> reserved, boolean avoidLiveTiles) {
		Vector2i spawn = GameMap.getSpawnPoint();
		if (isSpawnable(spawn, reserved, avoidLiveTiles)) {
			return spawn;
		}
		
		int width = GameMap.getWidth();
		int height = GameMap.getHeight();
		for (int radius = 1; radius < Math.max(width, height); radius++) {
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dy = -radius; dy <= radius; dy++) {
					if (Math.abs(dx) != radius && Math.abs(dy) != radius) {
						continue;
					}
					
					Vector2i candidate = new Vector2i(spawn.x + dx, spawn.y + dy);
					if (GameMap.contains(candidate) && isSpawnable(candidate, reserved, avoidLiveTiles)) {
						return candidate;
					}
				}
			}
		}
		return spawn;
	}
	
	private boolean isSpawnable(Vector2i coordinate, Set<Vector2i> reserved, boolean avoidLiveTiles) {
		return !reserved.contains(coordinate)
			&& !(avoidLiveTiles && foregroundGrid.hasTile(coordinate))
			&& !GameMap.isWall(coordinate);
	}
	
	private Packet createSnapshot() {
		List<Vector3i> players = new ArrayList<>();
		List<Vector2i> crates = new ArrayList<>();
		
		for (Vector2i coordinate : foregroundGrid.coordinateToTile.keySet()) {
			TileRenderer tile = foregroundGrid.getTile(coordinate);
			if (tile == null) {
				continue;
			}
			
			Entity entity = tile.getEntity();
			if (entity.hasTag(EntityTag.PLAYER)) {
				players.add(new Vector3i(entity.getComponent(NetworkIdentity.class).id, coordinate.x, coordinate.y));
			} else if (entity.hasTag(EntityTag.CRATE)) {
				crates.add(coordinate);
			}
		}
		
		return new GameStatePacket(players.toArray(new Vector3i[0]), crates.toArray(new Vector2i[0]));
	}
	
}
