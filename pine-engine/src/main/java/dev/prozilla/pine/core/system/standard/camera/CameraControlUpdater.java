package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Updates controllable cameras based on their target positions and zoom and their velocity.
 */
public class CameraControlUpdater extends UpdateSystem {
	
	public CameraControlUpdater() {
		super(CameraControlData.class, CameraData.class, Transform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getComponent(Transform.class);
		CameraData cameraData = chunk.getComponent(CameraData.class);
		CameraControlData cameraControlData = chunk.getComponent(CameraControlData.class);
		
		if (cameraControlData.disableControlsOnPause && application.isPaused) {
			transform.position.x = cameraControlData.targetX;
			transform.position.y = cameraControlData.targetY;
			cameraData.zoomFactor = cameraControlData.targetZoom;
			return;
		}
		
		if (cameraControlData.velocityDamping == 0) {
			transform.setPosition(cameraControlData.targetX, cameraControlData.targetY);
			cameraData.zoomFactor = cameraControlData.targetZoom;
		} else {
			// Calculate velocity towards the target position and zoom with damping
			float velocityX = (cameraControlData.targetX - transform.position.x) * cameraControlData.velocityDamping;
			float velocityY = (cameraControlData.targetY - transform.position.y) * cameraControlData.velocityDamping;
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
			
			float newX = MathUtils.clamp(transform.position.x, minX, maxX);
			float newY = MathUtils.clamp(transform.position.y, minY, maxY);
			transform.setPosition(newX, newY);
			
			cameraControlData.targetX = MathUtils.clamp(cameraControlData.targetX, minX, maxX);
			cameraControlData.targetY = MathUtils.clamp(cameraControlData.targetY, minY, maxY);
		}
	}
}
