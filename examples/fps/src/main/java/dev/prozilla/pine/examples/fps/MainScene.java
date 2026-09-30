package dev.prozilla.pine.examples.fps;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.prefab.mesh.MeshPrefab;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class MainScene extends Scene {
	
	private Vector2i previousCursorPosition;
	
	private float cameraYaw = -45f;
	private float cameraPitch = -30f;
	private float cameraDistance = 25f;
	
	private static final Vector3f CAMERA_CENTER = new Vector3f(0, 5, 0);
	private static final float ORBIT_SPEED = 3.75f;
	private static final float ZOOM_SPEED = 5f;
	private static final float MIN_DISTANCE = 3f;
	private static final float MAX_DISTANCE = 50f;
	
	@Override
	protected void load() {
		super.load();
		
		StaticMesh mesh = AssetPools.models.load("teapot.obj").getFirstMesh();
		MeshPrefab<StaticMesh> meshPrefab = new MeshPrefab<>(mesh);
		meshPrefab.setScale(new Vector3f(0.125f));
		addEntity(meshPrefab);
		
		updateCamera();
		
		getInput().disableCursor();
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
			cameraDistance -= scrollY * ZOOM_SPEED * deltaTime;
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
	
}
