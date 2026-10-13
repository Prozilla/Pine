package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraController3D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.init.InitSystemBase;

public final class CameraController3DInitializer extends InitSystemBase {
	
	public CameraController3DInitializer() {
		super(CameraController3D.class, Transform.class, CameraData.class);
	}
	
	@Override
	public void init() {
		forEach(chunk -> {
			Transform transform = chunk.getComponent(Transform.class);
			CameraController3D cameraControlData = chunk.getComponent(CameraController3D.class);
			
			cameraControlData.target.set(transform.position);
			cameraControlData.previousCursorPosition.set(application.getInput().getCursor(true));
		});
	}
}
