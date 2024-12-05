package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.KeyBindings;

public class CameraControlData extends Component {
	
	public float movementSpeed;
	public float velocityDamping;
	
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
	
	public KeyBindings<Action> keyBindings;
	
	public enum Action {
		FORWARD,
		BACKWARDS,
		RIGHT,
		LEFT,
		ZOOM_IN,
		ZOOM_OUT,
		SPEED_UP,
		SLOW_DOWN,
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
