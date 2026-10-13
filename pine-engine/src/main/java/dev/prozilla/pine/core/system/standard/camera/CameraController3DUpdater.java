package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraController3D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public final class CameraController3DUpdater extends UpdateSystem {
	
	public CameraController3DUpdater() {
		super(CameraController3D.class, CameraData.class, Transform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		CameraController3D cameraControlData = chunk.getComponent(CameraController3D.class);
		
		if (cameraControlData.disableControlsOnPause && application.isPaused()) {
			transform.position.set(cameraControlData.target);
			return;
		}
		
		if (cameraControlData.velocityDamping == 0) {
			transform.setPosition(cameraControlData.target);
		} else {
			Vector3f velocity = cameraControlData.target.clone().subtract(transform.position).scale(cameraControlData.velocityDamping);
			transform.translate(velocity);
		}
	}
}
