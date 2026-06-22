package dev.prozilla.pine.examples.fps;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.property.vector.delegated.DelegatedVector3fProperty;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.prefab.mesh.CuboidPrefab;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class GameScene extends Scene {
	
	private Vector2i previousCursorPosition;
	
	public static final float MOVEMENT_SPEED = 20f;
	public static final float ROTATION_SPEED = 6f;
	
	@Override
	protected void load() {
		super.load();
		previousCursorPosition = null;
		
		CuboidPrefab cubePrefab = new CuboidPrefab(new Cuboid(new Vector3f(10, 10, 10)), "textures/checker.png");
		
		world.addEntity(cubePrefab, 0, 5, -20);
		
		cubePrefab.setMesh(new Cuboid(new Vector3f(15, 15, 15)));
		cubePrefab.setRotation(new DelegatedVector3fProperty(
			new FixedFloatProperty(0),
			getTimer().scaledTimeProperty().multiply(100),
			new FixedFloatProperty(0)
		));
		world.addEntity(cubePrefab, 15, 7.5f, -35);
		
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
		Vector3f delta = new Vector3f();
		Transform cameraTransform = cameraData.getTransform();
		if (input.getKey(Key.W)) {
			delta.add(cameraTransform.getForward());
		}
		if (input.getKey(Key.S)) {
			delta.subtract(cameraTransform.getForward());
		}
		if (input.getKey(Key.D)) {
			delta.add(cameraTransform.getRight());
		}
		if (input.getKey(Key.A)) {
			delta.subtract(cameraTransform.getRight());
		}
		if (input.getKey(Key.E)) {
			delta.add(cameraTransform.getUp());
		}
		if (input.getKey(Key.Q)) {
			delta.subtract(cameraTransform.getUp());
		}
		if (!delta.isZero()) {
			delta.normalize();
			delta.scale(deltaTime * MOVEMENT_SPEED);
			if (input.getKey(Key.L_SHIFT)) {
				delta.scale(3f);
			}
			cameraTransform.translate(delta);
		}
		
		Vector2i cursorPosition = input.getCursor();
		if (previousCursorPosition != null) {
			Vector2i cursorMovement = previousCursorPosition.subtract(cursorPosition);
			cameraTransform.rotate(-cursorMovement.y * deltaTime * ROTATION_SPEED, -cursorMovement.x * deltaTime * ROTATION_SPEED, 0);
			previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
		} else {
			previousCursorPosition = cursorPosition.clone();
		}
	}
}
