package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.camera.CameraController2D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

/**
 * Handles input for controllable cameras.
 */
public final class CameraController2DInputHandler extends InputSystem {
	
	public CameraController2DInputHandler() {
		super(CameraController2D.class, CameraData.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Input input, float deltaTime) {
		CameraController2D cameraControlData = chunk.getComponent(CameraController2D.class);
		CameraData cameraData = chunk.getComponent(CameraData.class);
		
		if (!cameraControlData.disableControlsOnPause || !application.isPaused()) {
			handleMovementInput(deltaTime, cameraControlData, cameraData, input);
			handleZoomInput(deltaTime, cameraControlData, input);
		}
	}
	
	/**
	 * Handle camera movement input.
	 * @param deltaTime Delta time in seconds
	 */
	private void handleMovementInput(float deltaTime, CameraController2D cameraControlData, CameraData cameraData, Input input) {
		if (cameraControlData.movementSpeed == 0 || !cameraControlData.enableMovement)
			return;
		
		float deltaX = 0f, deltaY = 0f;
		
		// Handle movement by key presses
		if (cameraControlData.right.isPressed(input))
			deltaX += 1f;
		if (cameraControlData.left.isPressed(input))
			deltaX -= 1f;
		if (cameraControlData.forward.isPressed(input))
			deltaY += 1f;
		if (cameraControlData.backwards.isPressed(input))
			deltaY -= 1f;
		
		float movementFactor = (cameraControlData.movementSpeed / (cameraData.zoomFactor * cameraData.zoomFactor)) * deltaTime;
		
		// Apply speed modifiers
		if (cameraControlData.speedUp.isPressed(input)) {
			movementFactor *= 2f;
		} else if (cameraControlData.slowDown.isPressed(input)) {
			movementFactor *= 0.5f;
		}
		
		deltaX *= movementFactor;
		deltaY *= movementFactor;
		
		cameraControlData.targetX = cameraControlData.targetX + deltaX;
		cameraControlData.targetY = cameraControlData.targetY + deltaY;
	}
	
	/**
	 * Handle camera zoom input.
	 * @param deltaTime Delta time in seconds
	 */
	private void handleZoomInput(float deltaTime, CameraController2D cameraControlData, Input input) {
		if (cameraControlData.zoomSpeed == 0 || !cameraControlData.enableZoom)
			return;
		
		float deltaZoom = 0f;
		
		// Handle zooming by key presses
		if (cameraControlData.zoomIn.isPressed(input))
			deltaZoom += 0.01f;
		if (cameraControlData.zoomOut.isPressed(input))
			deltaZoom -= 0.01f;
		
		// Handle zooming by scrolling
		if (input.getScrollY() < 0) {
			deltaZoom -= 0.1f;
		} else if (input.getScrollY() > 0) {
			deltaZoom += 0.1f;
		}
		
		deltaZoom = deltaZoom * cameraControlData.zoomSpeed * deltaTime;
		
		// Apply speed modifiers
		if (cameraControlData.speedUp.isPressed(input)) {
			deltaZoom *= 2f;
		} else if (cameraControlData.slowDown.isPressed(input)) {
			deltaZoom *= 0.5f;
		}
		
		cameraControlData.targetZoom = MathUtils.clamp(cameraControlData.targetZoom + deltaZoom, cameraControlData.minZoom, cameraControlData.maxZoom);
	}
}
