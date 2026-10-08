package dev.prozilla.pine.examples.fps;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.mesh.MeshPrefab;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class SponzaScene extends Scene {
	
	private Vector2i previousCursorPosition;
	
	private float cameraYaw;
	private float cameraPitch;
	private float cameraDistance;
	
	public static final String MODEL_PATH = "sponza/sponza.gltf";
	private static final float SCALE = 0.05f;
	
	private static final Vector3f CAMERA_CENTER = new Vector3f(0, 10, 0);
	private static final float ORBIT_SPEED = 3.75f;
	private static final float ZOOM_SPEED = 50f;
	private static final float MIN_DISTANCE = 3f;
	private static final float MAX_DISTANCE = 100f;
	
	public SponzaScene() {
		cameraDistance = 25f;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Prefab parentPrefab = new Prefab();
		for (MeshPrefab<StaticMesh> meshPrefab : AssetPools.models.load(MODEL_PATH).createPrefabs()) {
			meshPrefab.setScale(new Vector3f(SCALE));
			parentPrefab.addChild(meshPrefab);
		}
		
		spawnObject(parentPrefab, new Vector3f(), new Vector3f());
		
		updateCamera();
		getInput().disableCursor();
	}
	
	private void spawnObject(Prefab prefab, Vector3f position, Vector3f rotation) {
		prefab.setPosition(position);
		prefab.setRotation(rotation);
		
		addEntity(prefab);
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.ESCAPE)) {
			application.stop();
			return;
		} else if (input.getKeyDown(Key.P) || input.getKeyDown(Key.SPACE)) {
			application.togglePause();
		} else if (input.getKeyDown(Key.F5)) {
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
	
	@Override
	public void render(Renderer renderer) throws IllegalStateException {
		super.render(renderer);
		
		ShaderProgram.getLit().setSunlightDirection(new Vector3f((float)Math.cos(getTimer().getScaledTime()), 1f, (float)Math.sin(getTimer().getScaledTime())));
	}
	
	private void updateCamera() {
		if (cameraData == null) {
			return;
		}
		
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
