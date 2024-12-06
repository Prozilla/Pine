package dev.prozilla.pine.core.system.camera;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.UpdateSystem;

public class CameraControlUpdater extends UpdateSystem {
	
	public CameraControlUpdater() {
		super(CameraControlData.class, CameraData.class, Transform.class);
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			Transform transform = match.getComponent(Transform.class);
			CameraData cameraData = match.getComponent(CameraData.class);
			CameraControlData cameraControlData = match.getComponent(CameraControlData.class);
			
			if (cameraControlData.velocityDamping == 0) {
				transform.setPosition(cameraControlData.targetX, cameraControlData.targetY);
				cameraData.zoomFactor = cameraControlData.targetZoom;
			} else {
				// Calculate velocity towards the target position and zoom with damping
				float velocityX = (cameraControlData.targetX - transform.x) * cameraControlData.velocityDamping;
				float velocityY = (cameraControlData.targetY - transform.y) * cameraControlData.velocityDamping;
				float velocityZoom = (cameraControlData.targetZoom - cameraData.zoomFactor) * cameraControlData.velocityDamping;
				
				// Apply velocities to update position and zoom smoothly
				transform.translate(velocityX, velocityY);
				cameraData.zoomFactor += velocityZoom;
			}
			
			// Clamp camera zoom to the allowed ranges
			cameraData.zoomFactor = MathUtils.clamp(cameraData.zoomFactor, cameraControlData.minZoom, cameraControlData.maxZoom);
			
			// Clamp camera position and target position to bounds
			if (cameraControlData.enableBounds) {
				float minX = cameraControlData.getMinX();
				float maxX = cameraControlData.getMaxX();
				float minY = cameraControlData.getMinY();
				float maxY = cameraControlData.getMaxY();
				
				float newX = MathUtils.clamp(transform.x, minX, maxX);
				float newY = MathUtils.clamp(transform.y, minY, maxY);
				transform.setPosition(newX, newY);
				
				cameraControlData.targetX = MathUtils.clamp(cameraControlData.targetX, minX, maxX);
				cameraControlData.targetY = MathUtils.clamp(cameraControlData.targetY, minY, maxY);
			}
		});
	}
}
