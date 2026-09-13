package dev.prozilla.pine.examples.tetris;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.examples.tetris.component.Grid;
import dev.prozilla.pine.examples.tetris.entity.GroundPrefab;
import dev.prozilla.pine.examples.tetris.entity.block.*;
import dev.prozilla.pine.examples.tetris.system.BlockInputHandler;
import dev.prozilla.pine.examples.tetris.system.BlockUpdater;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GameScene extends Scene {
	
	public List<Function<String, BlockPrefab>> blockPrefabs = List.of(
		FlatBlockPrefab::new,
		StraightBlockPrefab::new,
		LBlockPrefab::new,
		JBlockPrefab::new,
		TBlockPrefab::new,
		SBlockPrefab::new,
		ZBlockPrefab::new
	);
	
	private Vector2i previousCursorPosition;
	
	private Grid grid;
	
	private static final Random random = new Random();
	
	public static final int BLOCK_SPAWN_HEIGHT = 10;
	public static final List<String> TEXTURE_PATHS = List.of(
		"PNG/Default/element_red_square.png",
		"PNG/Default/element_yellow_square.png",
		"PNG/Default/element_green_square.png",
		"PNG/Default/element_blue_square.png",
		"PNG/Default/element_purple_square.png"
	);
	
	private static final Vector3f CAMERA_CENTER = new Vector3f(0, 5, 0);
	private static final float ORBIT_SPEED = 2.5f;
	private static final float ZOOM_SPEED = 5f;
	private static final float MIN_DISTANCE = 3f;
	private static final float MAX_DISTANCE = 50f;
	
	private float cameraYaw = -45f;
	private float cameraPitch = -30f;
	private float cameraDistance = 15f;
	
	public Grid getGrid() {
		return grid;
	}
	
	@Override
	protected void load() {
		super.load();
		previousCursorPosition = null;
		grid = new Grid();
		getInput().disableCursor();
		
		addSystem(new BlockUpdater());
		addSystem(new BlockInputHandler());
		
		addEntity(new GroundPrefab());
		
		spawnBlock();
		
		updateCamera();
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.ESCAPE)) {
			application.stop();
			return;
		}
		if (input.getKeyDown(Key.P)) {
			application.togglePause();
		}
		if (input.getKeyDown(Key.F5)) {
			application.reloadScene();
		}
		
		Vector2i cursorPosition = input.getCursor();
		if (previousCursorPosition != null) {
			Vector2i cursorMovement = previousCursorPosition.subtract(cursorPosition);
			cameraYaw += cursorMovement.x * deltaTime * ORBIT_SPEED;
			cameraPitch -= cursorMovement.y * deltaTime * ORBIT_SPEED;
			cameraPitch = MathUtils.clamp(cameraPitch, -89.9f, 89.9f);
			previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
		} else {
			previousCursorPosition = cursorPosition.clone();
		}
		
		float scrollY = input.getScrollY();
		if (scrollY != 0) {
			cameraDistance -= scrollY * ZOOM_SPEED;
			cameraDistance = MathUtils.clamp(cameraDistance, MIN_DISTANCE, MAX_DISTANCE);
		}
		
		updateCamera();
	}
	
	private void updateCamera() {
		Transform cameraTransform = cameraData.getTransform();
		
		float pitch = (float)Math.toRadians(cameraPitch);
		float yaw = (float)Math.toRadians(cameraYaw);
		float cosPitch = (float)Math.cos(pitch);
		
		float x = CAMERA_CENTER.x + cameraDistance * (float)Math.sin(yaw) * cosPitch;
		float y = CAMERA_CENTER.y + cameraDistance * (float)Math.sin(pitch);
		float z = CAMERA_CENTER.z + cameraDistance * (float)Math.cos(yaw) * cosPitch;
		
		cameraTransform.setPosition(x, y, z);
		
		Vector3f direction = new Vector3f(CAMERA_CENTER).subtract(cameraTransform.position).normalize();
		float newPitch = (float)Math.toDegrees(Math.asin(-direction.y));
		float newYaw = (float)Math.toDegrees(Math.atan2(direction.x, -direction.z));
		cameraTransform.setRotation(newPitch, newYaw, 0);
	}
	
	public void spawnBlock() {
		String texturePath = TEXTURE_PATHS.get(random.nextInt(TEXTURE_PATHS.size()));
		BlockPrefab blockPrefab = blockPrefabs.get(random.nextInt(blockPrefabs.size())).apply(texturePath);
		addEntity(blockPrefab, Grid.gridToWorldX(4), BLOCK_SPAWN_HEIGHT, Grid.gridToWorldZ(4));
	}
}
