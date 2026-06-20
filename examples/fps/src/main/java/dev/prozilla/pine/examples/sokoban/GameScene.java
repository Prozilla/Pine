package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.shape.RectPrefab;
import dev.prozilla.pine.core.rendering.shape.Rect;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class GameScene extends Scene {
	
	private final Vector2i previousCursorPosition = new Vector2i();
	
	public static final float MOVEMENT_SPEED = 5f;
	public static final float ROTATION_SPEED = 3f;
	
	@Override
	protected void load() {
		super.load();
		previousCursorPosition.set(0);
		
		Entity rect = world.addEntity(new RectPrefab(new Rect(new Vector2f(-5, -5), new Vector2f(10, 10)), Color.white()));
		rect.transform.translate(0, 0, -10);
		
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
		Vector3f delta = new Vector3f();
		if (input.getKey(Key.W)) {
			delta.z -= 1;
		}
		if (input.getKey(Key.S)) {
			delta.z += 1;
		}
		if (input.getKey(Key.A)) {
			delta.x -= 1;
		}
		if (input.getKey(Key.D)) {
			delta.x += 1;
		}
		if (input.getKey(Key.Q)) {
			delta.y -= 1;
		}
		if (input.getKey(Key.E)) {
			delta.y += 1;
		}
		if (input.getKey(Key.L_SHIFT)) {
			delta.scale(10);
		}
		if (!delta.isZero()) {
			delta.scale(deltaTime * MOVEMENT_SPEED);
			cameraData.getTransform().translate(delta);
		}
		
		Vector2i cursorPosition = input.getCursor();
		Vector2i cursorMovement = previousCursorPosition.subtract(cursorPosition);
		cameraData.getTransform().rotate(-cursorMovement.y * deltaTime * ROTATION_SPEED, -cursorMovement.x * deltaTime * ROTATION_SPEED, 0);
		previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
	}
}
