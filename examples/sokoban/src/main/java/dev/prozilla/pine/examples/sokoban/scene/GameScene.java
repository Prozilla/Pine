package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.ModifierKey;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.entity.ui.UIPrefab;
import dev.prozilla.pine.examples.sokoban.level.Level;
import dev.prozilla.pine.examples.sokoban.level.LevelParser;
import dev.prozilla.pine.examples.sokoban.packet.*;
import dev.prozilla.pine.examples.sokoban.request.MoveRequest;
import dev.prozilla.pine.examples.sokoban.request.RestartRequest;
import dev.prozilla.pine.examples.sokoban.request.UndoRequest;
import dev.prozilla.pine.examples.sokoban.system.CrateUpdater;
import dev.prozilla.pine.examples.sokoban.system.MessageHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerInputHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerMover;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;
import dev.prozilla.pine.extensions.pinet.entity.NetworkManagerPrefab;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageFilter;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageHandler;
import dev.prozilla.pine.extensions.pinet.message.ServerMessageLogger;
import dev.prozilla.pine.extensions.pinet.system.NetworkSynchronizer;

public class GameScene extends Scene {
	
	private Vector2i previousCursorPosition;
	private boolean cameraMovementEnabled = false;
	
	private NetworkManager network;
	private GridGroup foregroundGrid;
	private GridGroup goalGrid;
	private GridGroup backgroundGrid;
	
	public static final float MOVEMENT_SPEED = 300f;
	public static final float ROTATION_SPEED = 6f;
	
	private static final LevelParser levelParser = new LevelParser();
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.orthographic = true;
		
		if (GameManager.instance.isHost()) {
			GameManager.instance.level = levelParser.read(ArrayUtils.getRandom(Level.LEVELS));
		}
		
		// Create grid entities
		GridPrefab gridPrefab = new GridPrefab(GameManager.TILE_SIZE);
		backgroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		goalGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		foregroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		
		// Create network
		network = addEntity(new NetworkManagerPrefab()).getComponent(NetworkManager.class);
		network.getCodec()
			.addDecoder(MoveRequest.ID, MoveRequest::decode)
			.addDecoder(UndoRequest.ID, UndoRequest::decode)
			.addDecoder(RestartRequest.ID, RestartRequest::decode)
			.addDecoder(WelcomePacket.ID, WelcomePacket::decode)
			.addDecoder(GameStatePacket.ID, GameStatePacket::decode)
			.addDecoder(PlayerJoinPacket.ID, PlayerJoinPacket::decode)
			.addDecoder(PlayerLeavePacket.ID, PlayerLeavePacket::decode)
			.addDecoder(PlayerMovePacket.ID, PlayerMovePacket::decode)
			.addDecoder(RejectionPacket.ID, RejectionPacket::decode)
			.addDecoder(LevelPacket.ID, LevelPacket::decode)
			.addObjectCodec(Level.class, Level::encode, Level::decode);
		
		// Add systems
		addSystem(new NetworkSynchronizer());
		ServerMessageHandler messageHandler = addSystem(new MessageHandler(this))
			.then(ServerMessageFilter.unacknowledged())
			.then(new ServerMessageLogger(logger));
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
		
		// Connect to network
		if (GameManager.instance.isMultiplayer()) {
			GameManager.SessionConfig sessionConfig = GameManager.instance.getSessionConfig();
			
			if (GameManager.instance.isHost()) {
				network.createHost(sessionConfig.port(), messageHandler);
			} else {
				network.createClient(sessionConfig.address(), sessionConfig.port(), messageHandler);
			}
		} else {
			network.createStandalone(messageHandler);
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
		} else if (input.getKeyDown(Key.ESCAPE)) {
			GameManager.instance.leaveSession();
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
	
	public void resetCamera() {
		// Move camera to center of map
		int width = GameManager.instance.level.getWidth();
		int height = GameManager.instance.level.getHeight();
		
		float depthMultiplier = 1f;
		if (renderLayerUpdater != null) {
			depthMultiplier = renderLayerUpdater.getDepthMultiplier();
		}
		
		cameraData.getTransform().reset();
		cameraData.getTransform().translate((width * GameManager.TILE_SIZE) / 2f, (height * GameManager.TILE_SIZE) / 2f, 10f * depthMultiplier);
	}
	
	public NetworkManager getNetwork() {
		return network;
	}
	
	public GridGroup getForegroundGrid() {
		return foregroundGrid;
	}
	
	public GridGroup getGoalGrid() {
		return goalGrid;
	}
	
	public GridGroup getBackgroundGrid() {
		return backgroundGrid;
	}
	
	@Override
	public void destroy() {
		network = Destructible.destroy(network);
		super.destroy();
	}
	
}
