package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.audio.AudioEffectPlayer;
import dev.prozilla.pine.core.component.mesh.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.component.*;
import dev.prozilla.pine.examples.sokoban.net.packet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkSystem extends UpdateSystemBase {
	
	private final NetworkManager network;
	private final GridGroup foregroundGrid;
	private final RequestProcessor processor;
	
	private boolean disconnectHandled;
	
	public NetworkSystem(NetworkManager network, GridGroup foregroundGrid, RequestProcessor processor) {
		super(NetworkPlayer.class, PlayerData.class, TileRenderer.class, SpriteRenderer.class, AudioEffectPlayer.class, History.class);
		setRequiredTag(EntityTag.PLAYER);
		this.network = network;
		this.foregroundGrid = foregroundGrid;
		this.processor = processor;
	}
	
	@Override
	public void update(float deltaTime) {
		network.tick(this::handlePacket);
		
		if (!network.isConnected()) {
			handleHostDisconnect();
		}
	}
	
	private void handleHostDisconnect() {
		if (disconnectHandled) {
			return;
		}
		disconnectHandled = true;
		
		GameManager.instance.leaveSession();
	}
	
	@Override
	public boolean shouldRun() {
		return network.getSession() != null;
	}
	
	private void handlePacket(Packet packet) {
		switch (packet) {
			case WelcomePacket(int playerId) -> network.setLocalPlayerId(playerId);
			case GameStatePacket state -> applyGameState(state);
			case PlayerJoinPacket(int playerId, int x, int y) -> processor.applyPlayer(playerId, new Vector2i(x, y));
			case PlayerLeavePacket(int playerId) -> despawnPlayer(playerId);
			case PlayerMovePacket move -> applyMove(move);
			case RejectionPacket ignored -> rejectPendingMove(network.getLocalPlayerId());
			default -> {
			}
		}
	}
	
	private void applyGameState(GameStatePacket state) {
		clearAwaitingConfirm(network.getLocalPlayerId());
		
		Map<Integer, Vector2i> playerPositions = new HashMap<>();
		for (int i = 0; i < state.playerIds().length; i++) {
			playerPositions.put(state.playerIds()[i], new Vector2i(state.playerX()[i], state.playerY()[i]));
		}
		
		List<Vector2i> cratePositions = new ArrayList<>();
		for (int i = 0; i < state.crateX().length; i++) {
			cratePositions.add(new Vector2i(state.crateX()[i], state.crateY()[i]));
		}
		
		processor.applyState(playerPositions, cratePositions);
	}
	
	private void applyMove(PlayerMovePacket packet) {
		Move move = packet.move();
		
		int localId = network.getLocalPlayerId();
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
			chunk.getComponent(NetworkPlayer.class).awaitingConfirm = false;
		}
	}
	
	public EntityChunk getPlayer(int playerId) {
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
	
}
