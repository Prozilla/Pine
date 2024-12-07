package dev.prozilla.pine.core.entity.prefab.camera;

import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.entity.Entity;

public class ControllableCameraPrefab extends CameraPrefab {
	
	protected float movementSpeed;
	protected float velocityDamping;
	protected float zoomSpeed;
	protected float minZoom;
	protected float maxZoom;
	
	protected boolean enableBounds;
	protected float boundsX;
	protected float boundsY;
	protected float boundsWidth;
	protected float boundsHeight;
	
	/**
	 * @param movementSpeed Movement speed (Set to <code>0</code> to disable movement)
	 * @param velocityDamping Velocity damping (Set to <code>0</code> to disable damping)
	 * @param zoomSpeed Zoom speed (Set to <code>0</code> to disable zooming)
	 * @param minZoom Minimum zoom factor
	 * @param maxZoom Maximum zoom factor
	 */
	public ControllableCameraPrefab(float movementSpeed, float velocityDamping, float zoomSpeed, float minZoom, float maxZoom) {
		this.movementSpeed = movementSpeed;
		this.velocityDamping = velocityDamping;
		this.zoomSpeed = zoomSpeed;
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		
		enableBounds = false;
	}
	
	public void disableMovement() {
		setMovementSpeed(0);
	}
	
	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	public void disableVelocityDamping() {
		setVelocityDamping(0);
	}
	
	public void setVelocityDamping(float velocityDamping) {
		this.velocityDamping = velocityDamping;
	}
	
	public void disableZoom() {
		setZoom(0, 1, 1);
	}
	
	public void setZoom(float zoomSpeed, float minZoom, float maxZoom) {
		setZoomSpeed(zoomSpeed);
		setZoomRange(minZoom, maxZoom);
	}
	
	public void setZoomSpeed(float zoomSpeed) {
		this.zoomSpeed = zoomSpeed;
	}
	
	public void setZoomRange(float minZoom, float maxZoom) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
	}
	
	public void disableBounds() {
		enableBounds = false;
	}
	
	public void setBounds(float x, float y, float width, float height) {
		enableBounds = true;
		boundsX = x;
		boundsY = y;
		boundsWidth = width;
		boundsHeight = height;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CameraControlData cameraControlData = entity.addComponent(new CameraControlData());
		
		cameraControlData.movementSpeed = movementSpeed;
		cameraControlData.velocityDamping = velocityDamping;
		
		cameraControlData.zoomSpeed = zoomSpeed;
		cameraControlData.minZoom = minZoom;
		cameraControlData.maxZoom = maxZoom;
		
		cameraControlData.enableBounds = enableBounds;
		
		if (enableBounds) {
			cameraControlData.boundsX = boundsX;
			cameraControlData.boundsY = boundsY;
			cameraControlData.boundsWidth = boundsWidth;
			cameraControlData.boundsHeight = boundsHeight;
		}
	}
}
