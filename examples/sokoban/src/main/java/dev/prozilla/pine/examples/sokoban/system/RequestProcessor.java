package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameMap;
import dev.prozilla.pine.examples.sokoban.component.History;
import dev.prozilla.pine.examples.sokoban.component.Move;
import dev.prozilla.pine.examples.sokoban.component.NetworkPlayer;
import dev.prozilla.pine.examples.sokoban.component.PlayerData;
import dev.prozilla.pine.examples.sokoban.entity.CratePrefab;
import dev.prozilla.pine.examples.sokoban.entity.PlayerPrefab;
import dev.prozilla.pine.examples.sokoban.net.packet.*;

import java.util.*;

public class RequestProcessor extends UpdateSystemBase {
	
	public static final int HOST_PLAYER_ID = 0;
	
	private final GridGroup foregroundGrid;
	private final List<Vector2i> crateSpawns;
	private final Queue<Request> requests;
	private final Responder localResponder;
	private int nextPlayerId = 1;
	
	public RequestProcessor(GridGroup foregroundGrid) {
		super(NetworkPlayer.class, PlayerData.class, TileRenderer.class, History.class);
		setRequiredTag(EntityTag.PLAYER);
		this.foregroundGrid = foregroundGrid;
		
		crateSpawns = new ArrayList<>();
		for (Vector2i coordinate : foregroundGrid.coordinateToTile.keySet()) {
			TileRenderer tile = foregroundGrid.getTile(coordinate);
			if (tile != null && tile.getEntity().hasTag(EntityTag.CRATE)) {
				crateSpawns.add(tile.getCoordinate().clone());
			}
		}
		
		requests = new ArrayDeque<>();
		localResponder = new Responder() {
			@Override
			public void send(Packet packet) {
			}
			
			@Override
			public void broadcast(Packet packet) {
			}
			
			@Override
			public void broadcastOthers(Packet packet) {
			}
		};
	}
	
	@Override
	public void update(float deltaTime) {
		Request request;
		while ((request = requests.poll()) != null) {
			try {
				handle(request);
			} catch (RuntimeException e) {
				logger.error("Failed to handle request: " + request.packet().getClass().getSimpleName(), e);
			}
		}
	}
	
	public void receive(Packet packet) {
		receive(HOST_PLAYER_ID, packet, localResponder);
	}
	
	public void receive(int playerId, Packet packet, Responder responder) {
		requests.add(new Request(playerId, packet, responder));
	}
	
	public void applyState(Map<Integer, Vector2i> playerPositions, List<Vector2i> cratePositions) {
		forEach(chunk -> {
			int id = chunk.getComponent(NetworkPlayer.class).id;
			
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
	
	public int onJoin(boolean host, Responder responder) {
		int playerId = host ? HOST_PLAYER_ID : nextPlayerId++;
		
		Vector2i spawn = findSpawn();
		foregroundGrid.addTile(new PlayerPrefab(playerId), spawn.x, spawn.y);
		
		responder.send(new WelcomePacket(playerId));
		responder.send(createSnapshot());
		responder.broadcastOthers(new PlayerJoinPacket(playerId, spawn.x, spawn.y));
		
		return playerId;
	}
	
	public void onLeave(int playerId, Responder responder) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk != null) {
			chunk.getEntity().destroy();
		}
		
		responder.broadcastOthers(new PlayerLeavePacket(playerId));
	}
	
	public Vector2i getPlayerPosition(int playerId) {
		EntityChunk chunk = getPlayer(playerId);
		return chunk != null ? chunk.getComponent(TileRenderer.class).getCoordinate() : null;
	}
	
	private void handle(Request request) {
		Packet packet = request.packet();
		
		if (packet instanceof MoveRequestPacket(Direction direction)) {
			move(request.playerId(), direction, request.responder());
		} else if (packet instanceof UndoRequestPacket) {
			undo(request.playerId(), request.responder());
		} else if (packet instanceof RestartRequestPacket) {
			restart(request.playerId(), request.responder());
		}
	}
	
	private void move(int playerId, Direction direction, Responder responder) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk == null) {
			fail(playerId, responder);
			return;
		}
		
		History history = chunk.getComponent(History.class);
		NetworkPlayer networkPlayer = chunk.getComponent(NetworkPlayer.class);
		PlayerData playerData = chunk.getComponent(PlayerData.class);
		
		if (!networkPlayer.awaitingConfirm) {
			playerData.finishMove();
		}
		
		Move move = playerData.computeMove(foregroundGrid, direction);
		if (move == null) {
			fail(playerId, responder);
			return;
		}
		
		history.push(move);
		if (!networkPlayer.awaitingConfirm) {
			playerData.beginMove(move, foregroundGrid);
		}
		responder.broadcast(new PlayerMovePacket(move));
		
		if (responder == localResponder) {
			networkPlayer.awaitingConfirm = false;
		}
	}
	
	private void undo(int playerId, Responder responder) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk == null) {
			fail(playerId, responder);
			return;
		}
		
		chunk.getComponent(PlayerData.class).finishMove();
		
		if (chunk.getComponent(History.class).undo(foregroundGrid) != null) {
			responder.broadcast(createSnapshot());
		} else {
			fail(playerId, responder);
		}
	}
	
	private void restart(int playerId, Responder responder) {
		boolean allowed = responder == localResponder || playerId == HOST_PLAYER_ID;
		if (!allowed) {
			fail(playerId, responder);
			return;
		}
		
		Map<Integer, Vector2i> playerPositions = new HashMap<>();
		Set<Vector2i> reserved = new HashSet<>(crateSpawns);
		forEach(chunk -> {
			int id = chunk.getComponent(NetworkPlayer.class).id;
			Vector2i spawn = findSpawn(reserved, false);
			reserved.add(spawn);
			
			playerPositions.put(id, spawn);
		});
		
		applyState(playerPositions, crateSpawns);
		
		forEach(chunk -> chunk.getComponent(History.class).clear());
		
		responder.broadcast(createSnapshot());
	}
	
	
	private void fail(int playerId, Responder responder) {
		if (responder == localResponder) {
			rejectPendingMove(playerId);
			return;
		}
		
		responder.send(new RejectionPacket());
		responder.send(createSnapshot());
	}
	
	private void rejectPendingMove(int playerId) {
		EntityChunk chunk = getPlayer(playerId);
		if (chunk == null) {
			return;
		}
		
		chunk.getComponent(History.class).revertPending(foregroundGrid);
		chunk.getComponent(NetworkPlayer.class).awaitingConfirm = false;
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
	
	private EntityChunk getPlayer(int playerId) {
		EntityChunk result = null;
		
		if (hasEntityChunks()) {
			for (EntityChunk chunk : getChunks()) {
				if (chunk.getComponent(NetworkPlayer.class).id == playerId) {
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
				players.add(new int[] { entity.getComponent(NetworkPlayer.class).id, coordinate.x, coordinate.y });
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
	
	public interface Responder {
		
		void send(Packet packet);
		
		void broadcast(Packet packet);
		
		void broadcastOthers(Packet packet);
		
	}
	
	private record Request(int playerId, Packet packet, Responder responder) {
	}
	
}
