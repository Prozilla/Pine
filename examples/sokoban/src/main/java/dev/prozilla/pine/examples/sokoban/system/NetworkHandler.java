package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.GameMap;
import dev.prozilla.pine.examples.sokoban.component.History;
import dev.prozilla.pine.examples.sokoban.component.Move;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;
import dev.prozilla.pine.examples.sokoban.entity.CratePrefab;
import dev.prozilla.pine.examples.sokoban.entity.PlayerPrefab;
import dev.prozilla.pine.examples.sokoban.packet.*;
import dev.prozilla.pine.extensions.pinet.client.ClientPacketHandler;
import dev.prozilla.pine.extensions.pinet.component.NetworkIdentity;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.server.Server;
import dev.prozilla.pine.extensions.pinet.server.ServerRequestHandler;

import java.util.*;

public class NetworkHandler extends UpdateSystemBase implements ClientPacketHandler, ServerRequestHandler {
	
	private final NetworkManager network;
	private final GridGroup foregroundGrid;
	private final List<Vector2i> crateSpawns;
	
	private boolean disconnectHandled;
	
	public NetworkHandler(NetworkManager network, GridGroup foregroundGrid) {
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
	public void update(float deltaTime) {
		if (!network.isConnected() && !disconnectHandled) {
			disconnectHandled = true;
			GameManager.instance.leaveSession();
		}
	}
	
	@Override
	public boolean shouldRun() {
		return network.getSession() != null;
	}
	
	@Override
	public void handleRequest(Server.Request request) {
		Packet payload = request.getPayload();
		
		if (payload instanceof MoveRequestPacket(Direction direction)) {
			move(request, direction);
		} else if (payload instanceof UndoRequestPacket) {
			undo(request);
		} else if (payload instanceof RestartRequestPacket) {
			restart(request);
		}
	}
	
	@Override
	public void handleJoin(Server.Request request) {
		Vector2i spawn = findSpawn();
		foregroundGrid.addTile(new PlayerPrefab(request.getAuthorId()), spawn.x, spawn.y);
		
		request.reply(new WelcomePacket(request.getAuthorId()));
		request.reply(createSnapshot());
		request.replyToOthers(new PlayerJoinPacket(request.getAuthorId(), spawn.x, spawn.y));
	}
	
	@Override
	public void handleLeave(Server.Request request) {
		EntityChunk chunk = getPlayer(request.getAuthorId());
		if (chunk != null) {
			chunk.getEntity().destroy();
		}
		
		request.replyToOthers(new PlayerLeavePacket(request.getAuthorId()));
	}
	
	@Override
	public void handlePacket(Packet packet) {
		switch (packet) {
			case WelcomePacket(int playerId) -> network.setLocalClientId(playerId);
			case GameStatePacket state -> applyGameState(state);
			case PlayerJoinPacket(int playerId, int x, int y) -> applyPlayer(playerId, new Vector2i(x, y));
			case PlayerLeavePacket(int playerId) -> despawnPlayer(playerId);
			case PlayerMovePacket move -> applyMove(move);
			case RejectionPacket ignored -> rejectPendingMove(network.getLocalClientId());
			default -> {}
		}
	}
	
	private void applyGameState(GameStatePacket state) {
		clearAwaitingConfirm(network.getLocalClientId());
		
		Map<Integer, Vector2i> playerPositions = new HashMap<>();
		for (int i = 0; i < state.playerIds().length; i++) {
			playerPositions.put(state.playerIds()[i], new Vector2i(state.playerX()[i], state.playerY()[i]));
		}
		
		List<Vector2i> cratePositions = new ArrayList<>();
		for (int i = 0; i < state.crateX().length; i++) {
			cratePositions.add(new Vector2i(state.crateX()[i], state.crateY()[i]));
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
		if (coordinate.x == move.toX() && coordinate.y == move.toY()) {
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
	
	public void applyState(Map<Integer, Vector2i> playerPositions, List<Vector2i> cratePositions) {
		forEach(chunk -> {
			int id = chunk.getComponent(NetworkIdentity.class).id;
			
			if (!playerPositions.containsKey(id)) {
				chunk.getEntity().destroy();
				return;
			}
			
			foregroundGrid.removeTile(chunk.getComponent(TileRenderer.class));
		});
		
		clearCrates();
		for (Vector2i cratePosition : cratePositions) {
			foregroundGrid.addTile(new CratePrefab(), cratePosition.x, cratePosition.y);
		}
		
		for (Map.Entry<Integer, Vector2i> entry : playerPositions.entrySet()) {
			applyPlayer(entry.getKey(), entry.getValue());
		}
	}
	
	public void applyPlayer(int playerId, Vector2i position) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk != null) {
			chunk.getComponent(PlayerData.class).teleportTo(foregroundGrid, position.x, position.y);
			return;
		}
		
		if (!foregroundGrid.hasTile(position.x, position.y)) {
			foregroundGrid.addTile(new PlayerPrefab(playerId), position.x, position.y);
		}
	}
	
	private void move(Server.Request request, Direction direction) {
		EntityChunk chunk = getPlayer(request.getAuthorId());
		if (chunk == null) {
			fail(request);
			return;
		}
		
		History history = chunk.getComponent(History.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (!playerData.awaitingConfirm) {
			playerData.finishMove();
		}
		
		Move move = playerData.computeMove(foregroundGrid, direction);
		if (move == null) {
			fail(request);
			return;
		}
		
		history.push(move);
		if (!playerData.awaitingConfirm) {
			playerData.beginMove(move, foregroundGrid);
		}
		request.replyToAll(new PlayerMovePacket(move));
		
		if (request.isLocal()) {
			playerData.awaitingConfirm = false;
		}
	}
	
	private void undo(Server.Request request) {
		EntityChunk chunk = getPlayer(request.getAuthorId());
		if (chunk == null) {
			fail(request);
			return;
		}
		
		chunk.getComponent(PlayerData.class).finishMove();
		
		if (chunk.getComponent(History.class).undo(foregroundGrid) != null) {
			request.replyToAll(createSnapshot());
		} else {
			fail(request);
		}
	}
	
	private void restart(Server.Request request) {
		boolean allowed = request.receivedFromHost();
		if (!allowed) {
			fail(request);
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
	
	private void fail(Server.Request request) {
		if (request.isLocal()) {
			rejectPendingMove(request.getAuthorId());
			return;
		}
		
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
					if (GameMap.contains(candidate.x, candidate.y) && isSpawnable(candidate, reserved, avoidLiveTiles)) {
						return candidate;
					}
				}
			}
		}
		return spawn;
	}
	
	private boolean isSpawnable(Vector2i candidate, Set<Vector2i> reserved, boolean avoidLiveTiles) {
		return !reserved.contains(candidate)
			       && !(avoidLiveTiles && foregroundGrid.hasTile(candidate.x, candidate.y))
			       && !GameMap.isWall(candidate.x, candidate.y);
	}
	
	private Packet createSnapshot() {
		List<int[]> players = new ArrayList<>();
		List<Vector2i> crates = new ArrayList<>();
		
		for (Vector2i coordinate : foregroundGrid.coordinateToTile.keySet()) {
			TileRenderer tile = foregroundGrid.getTile(coordinate);
			if (tile == null) {
				continue;
			}
			
			Entity entity = tile.getEntity();
			if (entity.hasTag(EntityTag.PLAYER)) {
				players.add(new int[] { entity.getComponent(NetworkIdentity.class).id, coordinate.x, coordinate.y });
			} else if (entity.hasTag(EntityTag.CRATE)) {
				crates.add(coordinate);
			}
		}
		
		players.sort(Comparator.comparingInt(player -> player[0]));
		
		int[] playerIds = new int[players.size()];
		int[] playerX = new int[players.size()];
		int[] playerY = new int[players.size()];
		for (int i = 0; i < players.size(); i++) {
			playerIds[i] = players.get(i)[0];
			playerX[i] = players.get(i)[1];
			playerY[i] = players.get(i)[2];
		}
		
		int[] crateX = new int[crates.size()];
		int[] crateY = new int[crates.size()];
		for (int i = 0; i < crates.size(); i++) {
			crateX[i] = crates.get(i).x;
			crateY[i] = crates.get(i).y;
		}
		
		return new GameStatePacket(playerIds, playerX, playerY, crateX, crateY);
	}
	
}
