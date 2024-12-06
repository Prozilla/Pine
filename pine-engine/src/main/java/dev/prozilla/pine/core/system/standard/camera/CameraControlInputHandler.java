package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

/**
 * Handles input for controllable cameras.
 */
public class CameraControlInputHandler extends InputSystem {
	
	public CameraControlInputHandler() {
		super(CameraControlData.class, CameraData.class);
	}
	
	@Override
	public void process(EntityMatch match, Input input, float deltaTime) {
		CameraControlData cameraControlData = match.getComponent(CameraControlData.class);
		CameraData cameraData = match.getComponent(CameraData.class);
		
		handleMovementInput(deltaTime, cameraControlData, cameraData);
		handleZoomInput(deltaTime, cameraControlData, input);
	}
	
	/**
	 * Handle camera movement input.
	 * @param deltaTime Delta time in seconds
	 */
	private void handleMovementInput(float deltaTime, CameraControlData cameraControlData, CameraData cameraData) {
		if (cameraControlData.movementSpeed == 0)
			return;
		
		float deltaX = 0f, deltaY = 0f;
		
		// Handle movement by key presses
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.RIGHT))
			deltaX += 1f;
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.LEFT))
			deltaX -= 1f;
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.FORWARD))
			deltaY += 1f;
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.BACKWARDS))
			deltaY -= 1f;
		
		float movementFactor = (cameraControlData.movementSpeed / (cameraData.zoomFactor * cameraData.zoomFactor)) * deltaTime;
		
		// Apply speed modifiers
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.SPEED_UP)) {
			movementFactor *= 2f;
		} else if (cameraControlData.keyBindings.isActive(CameraControlData.Action.SLOW_DOWN)) {
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
	private void handleZoomInput(float deltaTime, CameraControlData cameraControlData, Input input) {
		if (cameraControlData.zoomSpeed == 0)
			return;
		
		float deltaZoom = 0f;
		
		// Handle zooming by key presses
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.ZOOM_IN))
			deltaZoom += 0.01f;
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.ZOOM_OUT))
			deltaZoom -= 0.01f;
		
		// Handle zooming by scrolling
		if (input.getScrollY() < 0) {
			deltaZoom -= 0.1f;
		} else if (input.getScrollY() > 0) {
			deltaZoom += 0.1f;
		}
		
		deltaZoom = deltaZoom * cameraControlData.zoomSpeed * deltaTime;
		
		// Apply speed modifiers
		if (cameraControlData.keyBindings.isActive(CameraControlData.Action.SPEED_UP)) {
			deltaZoom *= 2f;
		} else if (cameraControlData.keyBindings.isActive(CameraControlData.Action.SLOW_DOWN)) {
			deltaZoom *= 0.5f;
		}
		
		cameraControlData.targetZoom = MathUtils.clamp(cameraControlData.targetZoom + deltaZoom, cameraControlData.minZoom, cameraControlData.maxZoom);
	}
}
