package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.ModifierKey;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.GameMap;
import dev.prozilla.pine.examples.sokoban.entity.*;
import dev.prozilla.pine.examples.sokoban.entity.ui.UIPrefab;
import dev.prozilla.pine.examples.sokoban.net.component.NetworkManager;
import dev.prozilla.pine.examples.sokoban.net.entity.NetworkManagerPrefab;
import dev.prozilla.pine.examples.sokoban.net.system.NetworkSynchronizer;
import dev.prozilla.pine.examples.sokoban.packet.*;
import dev.prozilla.pine.examples.sokoban.system.CrateUpdater;
import dev.prozilla.pine.examples.sokoban.system.NetworkHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerInputHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerMover;

public class GameScene extends Scene {
	
	private Vector2i previousCursorPosition;
	private boolean cameraMovementEnabled = false;
	
	private NetworkManager network;
	
	public static final float MOVEMENT_SPEED = 300f;
	public static final float ROTATION_SPEED = 6f;
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.orthographic = true;
		
		boolean isMultiplayer = GameManager.instance.isMultiplayer();
		boolean isHost = isMultiplayer && GameManager.instance.getSessionConfig().hosting();
		
		// Create grid entities
		GridPrefab gridPrefab = new GridPrefab(GameManager.TILE_SIZE);
		GridGroup backgroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup goalGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup foregroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		
		// Create network
		network = addEntity(new NetworkManagerPrefab()).getComponent(NetworkManager.class);
		network.getCodec()
			.addDecoder(MoveRequestPacket.ID, MoveRequestPacket::decode)
			.addDecoder(UndoRequestPacket.ID, UndoRequestPacket::decode)
			.addDecoder(RestartRequestPacket.ID, RestartRequestPacket::decode)
			.addDecoder(WelcomePacket.ID, WelcomePacket::decode)
			.addDecoder(GameStatePacket.ID, GameStatePacket::decode)
			.addDecoder(PlayerJoinPacket.ID, PlayerJoinPacket::decode)
			.addDecoder(PlayerLeavePacket.ID, PlayerLeavePacket::decode)
			.addDecoder(PlayerMovePacket.ID, PlayerMovePacket::decode)
			.addDecoder(RejectionPacket.ID, RejectionPacket::decode);
		
		// Create tile entities
		BlockPrefab blockPrefab = new BlockPrefab();
		GroundPrefab groundPrefab = new GroundPrefab();
		GoalPrefab goalPrefab = new GoalPrefab();
		PlayerPrefab playerPrefab = new PlayerPrefab();
		CratePrefab cratePrefab = new CratePrefab();
		
		GameManager.instance.totalCrates = 0;
		String[] map = GameMap.MAP;
		
		for (int i = 0; i < map.length; i++) {
			String row = map[i];
			
			for (int j = 0; j < row.length(); j++) {
				char tileName = row.charAt(j);
				
				if (tileName == '.') {
					goalGrid.addTile(goalPrefab, j, i);
				} else {
					TilePrefab tilePrefab = switch (tileName) {
						case 'O' -> blockPrefab;
						case 's' -> isMultiplayer ? null : playerPrefab;
						case 'x' -> {
							GameManager.instance.totalCrates++;
							yield isMultiplayer && !isHost ? null : cratePrefab;
						}
						default -> null;
					};
					
					if (tilePrefab != null) {
						foregroundGrid.addTile(tilePrefab, j, i);
					}
				}
				
				backgroundGrid.addTile(groundPrefab, j, i);
			}
		}
		
		// Add systems
		addSystem(new NetworkSynchronizer());
		NetworkHandler packetHandler = addSystem(new NetworkHandler(network, foregroundGrid));
		addSystem(new PlayerInputHandler(foregroundGrid, network));
		addSystem(new PlayerMover());
		addSystem(new CrateUpdater(goalGrid));
		
		// Add user interface
		StyleSheet styleSheet = AssetPools.styleSheets.load("style/hud.css", Application.isDevMode());
		addEntity(new UIPrefab(styleSheet));
		
		cameraData.zoomIn(-0.1f);
		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);
		cameraData.farClipPlane = 10000f;
		overlayCameraData.farClipPlane = 10000f;
		
		if (renderLayerUpdater != null) {
			renderLayerUpdater.setDepthMultiplier(25f);
		}
		
		resetCamera();
		
		// Connect to network
		if (isMultiplayer) {
			GameManager.SessionConfig sessionConfig = GameManager.instance.getSessionConfig();
			
			if (isHost) {
				network.createHost(sessionConfig.port(), packetHandler, packetHandler);
			} else {
				network.createClient(sessionConfig.address(), sessionConfig.port(), packetHandler);
			}
		} else {
			network.createStandalone(packetHandler);
		}
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.F) || (input.getKeyDown(Key.ESCAPE) && cameraMovementEnabled)) {
			cameraMovementEnabled = !cameraMovementEnabled;
			if (cameraMovementEnabled) {
				input.disableCursor();
				previousCursorPosition = null;
				cameraData.orthographic = false;
			} else {
				input.showCursor();
				cameraData.orthographic = true;
				resetCamera();
			}
		}
		
		if (cameraMovementEnabled) {
			Vector3f delta = new Vector3f();
			Transform cameraTransform = cameraData.getTransform();
			if (input.getKey(Key.UP_ARROW)) {
				delta.add(cameraTransform.getForward());
			}
			if (input.getKey(Key.DOWN_ARROW)) {
				delta.subtract(cameraTransform.getForward());
			}
			if (input.getKey(Key.RIGHT_ARROW)) {
				delta.add(cameraTransform.getRight());
			}
			if (input.getKey(Key.LEFT_ARROW)) {
				delta.subtract(cameraTransform.getRight());
			}
			if (input.getKey(Key.PAGE_UP)) {
				delta.add(cameraTransform.getUp());
			}
			if (input.getKey(Key.PAGE_DOWN)) {
				delta.subtract(cameraTransform.getUp());
			}
			if (!delta.isZero()) {
				delta.normalize();
				delta.scale(deltaTime * MOVEMENT_SPEED);
				if (input.getModifierKey(ModifierKey.SHIFT)) {
					delta.scale(3f);
				}
				cameraTransform.translate(delta);
			}
			
			Vector2i cursorPosition = input.getCursor(true);
			if (previousCursorPosition != null) {
				Vector2i cursorMovement = previousCursorPosition.subtract(cursorPosition);
				cameraTransform.rotate(-cursorMovement.y * deltaTime * ROTATION_SPEED, -cursorMovement.x * deltaTime * ROTATION_SPEED, 0);
				previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
			} else {
				previousCursorPosition = cursorPosition.clone();
			}
		}
	}
	
	private void resetCamera() {
		// Move camera to center of map
		int width = GameMap.getWidth();
		int height = GameMap.getHeight();
		
		float depthMultiplier = 1f;
		if (renderLayerUpdater != null) {
			depthMultiplier = renderLayerUpdater.getDepthMultiplier();
		}
		
		cameraData.getTransform().reset();
		cameraData.getTransform().translate((width * GameManager.TILE_SIZE) / 2f, (height * GameManager.TILE_SIZE) / 2f, 10f * depthMultiplier);
	}
	
	@Override
	public void destroy() {
		network = Destructible.destroy(network);
		super.destroy();
	}
	
}
