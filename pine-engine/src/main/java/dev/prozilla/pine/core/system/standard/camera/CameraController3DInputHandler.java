package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraController3D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class CameraController3DInputHandler extends InputSystem {
	
	public CameraController3DInputHandler() {
		super(CameraController3D.class, CameraData.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Input input, float deltaTime) {
		CameraController3D cameraControlData = chunk.getComponent(CameraController3D.class);
		
		if (!cameraControlData.disableControlsOnPause || !application.isPaused()) {
			handleTranslationInput(input, deltaTime, cameraControlData);
			handleRotationInput(input, cameraControlData);
		} else {
			cameraControlData.previousCursorPosition.set(input.getCursor(false));
		}
	}
	
	private void handleTranslationInput(Input input, float deltaTime, CameraController3D cameraControlData) {
		if (cameraControlData.translationSpeed == 0)
			return;
		
		Vector3f delta = new Vector3f();
		
		// Handle movement by key presses
		if (cameraControlData.right.isPressed(input))
			delta.x += 1f;
		if (cameraControlData.left.isPressed(input))
			delta.x -= 1f;
		if (cameraControlData.forward.isPressed(input))
			delta.z += 1f;
		if (cameraControlData.backwards.isPressed(input))
			delta.z -= 1f;
		if (cameraControlData.up.isPressed(input))
			delta.y += 1f;
		if (cameraControlData.down.isPressed(input))
			delta.y -= 1f;
		
		float factor = cameraControlData.translationSpeed * deltaTime;
		
		// Apply speed modifiers
		if (cameraControlData.speedUp.isPressed(input)) {
			factor *= 2f;
		} else if (cameraControlData.slowDown.isPressed(input)) {
			factor *= 0.5f;
		}
		
		delta.scale(factor);
		
		Transform transform = cameraControlData.getTransform();
		Vector3f forward = transform.getForward();
		Vector3f right = transform.getRight();
		Vector3f up = transform.getUp();

		float moveX = right.x * delta.x + up.x * delta.y + forward.x * delta.z;
		float moveY = right.y * delta.x + up.y * delta.y + forward.y * delta.z;
		float moveZ = right.z * delta.x + up.z * delta.y + forward.z * delta.z;
		
		cameraControlData.target.add(moveX, moveY, moveZ);
	}
	
	private void handleRotationInput(Input input, CameraController3D cameraControlData) {
		if (cameraControlData.rotationSpeed == 0)
			return;
		
		Vector2i cursorPosition = input.getCursor(true);
		Vector2i cursorMovement = cameraControlData.previousCursorPosition.clone().subtract(cursorPosition);
		float yaw = cursorMovement.x;
		float pitch = cursorMovement.y;
		cameraControlData.previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
		
		float factor = -cameraControlData.rotationSpeed;
		yaw *= factor;
		pitch *= factor;
		
		cameraControlData.getTransform().rotate(pitch, yaw, 0);
	}
}
