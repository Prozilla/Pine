package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraController2D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.init.InitSystemBase;

/**
 * Initializes controllable cameras by settings their keybindings and initial positions and zoom.
 */
public final class CameraController2DInitializer extends InitSystemBase {
	
	public CameraController2DInitializer() {
		super(CameraController2D.class, Transform.class, CameraData.class);
	}
	
	@Override
	public void init() {
		forEach(chunk -> {
			Transform transform = chunk.getComponent(Transform.class);
			CameraData cameraData = chunk.getComponent(CameraData.class);
			CameraController2D cameraControlData = chunk.getComponent(CameraController2D.class);
			
			cameraControlData.targetX = transform.position.x;
			cameraControlData.targetY = transform.position.y;
			cameraControlData.targetZoom = cameraData.zoomFactor;
		});
	}
}
