package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.property.input.InputBinding;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Key;

public class CameraController2D extends Component {
	
	public float movementSpeed;
	public float velocityDamping;
	public boolean disableControlsOnPause;
	
	public boolean enableZoom;
	public boolean enableMovement;
	
	public float zoomSpeed;
	public float minZoom;
	public float maxZoom;
	
	public float targetX;
	public float targetY;
	public float targetZoom;
	
	public boolean enableBounds;
	public float boundsX;
	public float boundsY;
	public float boundsWidth;
	public float boundsHeight;
	
	public final InputBinding forward;
	public final InputBinding backwards;
	public final InputBinding left;
	public final InputBinding right;
	public final InputBinding zoomIn;
	public final InputBinding zoomOut;
	public final InputBinding speedUp;
	public final InputBinding slowDown;
	
	public CameraController2D() {
		enableZoom = true;
		enableMovement = true;
		enableBounds = false;
		
		forward = new InputBinding(Key.W);
		backwards = new InputBinding(Key.S);
		left = new InputBinding(Key.A);
		right = new InputBinding(Key.D);
		zoomIn = new InputBinding(Key.X);
		zoomOut = new InputBinding(Key.Z);
		speedUp = new InputBinding(Key.L_SHIFT);
		slowDown = new InputBinding(Key.R_SHIFT);
	}
	
	/**
	 * Returns the minimum X position for the camera.
	 * @return X position
	 */
	public float getMinX() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsX;
	}
	
	/**
	 * Returns the maximum X position for the camera.
	 * @return X position
	 */
	public float getMaxX() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsX + boundsWidth / 2f;
	}
	
	/**
	 * Returns the minimum Y position for the camera.
	 * @return Y position
	 */
	public float getMinY() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsY;
	}
	
	/**
	 * Returns the maximum Y position for the camera.
	 * @return Y position
	 */
	public float getMaxY() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsY + boundsHeight / 2f;
	}
	
	/**
	 * Sets the bounds of this camera.
	 */
	public void setBounds(float x, float y, float width, float height) {
		enableBounds = true;
		boundsX = x;
		boundsY = y;
		boundsWidth = width;
		boundsHeight = height;
	}
}
