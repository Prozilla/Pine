package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

import java.awt.*;

public class CameraData extends Component {
	
	public float zoomFactor;
	
	public float width;
	public float height;
	
	public Color backgroundColor;
	
	public CameraData() {
		this(Color.black());
	}
	
	public CameraData(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		zoomFactor = 1;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	/**
	 * Zooms the camera by a factor.
	 * @param factor Zoom factor
	 */
	public void multiplyZoom(float factor) {
		zoomFactor *= factor;
	}
	
	/**
	 * Zooms the camera in or out based on a zoom value.
	 * Use a positive value to zoom in and a negative value to zoom out.
	 * @param zoom Zoom value
	 */
	public void zoomIn(float zoom) {
		zoomFactor = zoomFactor + zoom;
	}
	
	/**
	 * Returns the scale of the camera's viewport.
	 * @return Scale
	 */
	public float getScale() {
		return Math.max(width, height) / 800f;
	}
	
	/**
	 * Returns the zoom value multiplied by the scale of the viewport.
	 * @return Zoom value
	 */
	public float getZoom() {
		return zoomFactor * zoomFactor * zoomFactor * getScale();
	}
	
	/**
	 * Returns the horizontal center of this camera.
	 * @return X value
	 */
	public float getCenterX() {
		return width / 2f;
	}
	
	/**
	 * Returns the vertical center of this camera.
	 * @return Y value
	 */
	public float getCenterY() {
		return height / 2f;
	}
	
	/**
	 * Applies the camera's transformation to an x and y value.
	 * @param x X value
	 * @param y Y value
	 * @return Array of x and y values
	 */
	public Vector2f applyTransform(float x, float y) {
		x = (x - entity.transform.getGlobalX()) * getZoom() + getCenterX();
		y = (y - entity.transform.getGlobalY()) * getZoom() + getCenterY();
		
		return new Vector2f(x, y);
	}
	
	public Vector2f screenToWorldPosition(Point screenPosition) {
		if (screenPosition == null) {
			return new Vector2f();
		}
		
		return screenToWorldPosition(screenPosition.x, screenPosition.y);
	}
	
	public Vector2f screenToWorldPosition(int screenX, int screenY) {
		float x = ((float)screenX - getCenterX()) / getZoom() + entity.transform.getGlobalX();
		float y = (getCenterY() - (float)screenY) / getZoom() + entity.transform.getGlobalY();
		
		return new Vector2f(x, y);
	}
}
