package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraData extends Component {
	
	public float zoomFactor;
	
	public boolean orthographic;
	public float fieldOfView;
	public float width;
	public float height;
	public float nearClipPlane;
	public float farClipPlane;
	
	private final Matrix4f viewMatrix;
	private final Matrix4f projectionMatrix;
	
	public Color backgroundColor;
	
	public CameraData() {
		this(Color.black());
	}
	
	public CameraData(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		zoomFactor = 1;
		
		orthographic = false;
		fieldOfView = 40;
		nearClipPlane = 0.01f;
		farClipPlane = 1000f;
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
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
	 * Applies the camera's transformation to a given x and y value.
	 * @return Array of x and y values
	 */
	public Vector2f applyTransform(float x, float y) {
		return applyTransform(new Vector2f(x, y));
	}
	
	/**
	 * Applies the camera's transformation to a given position.
	 * @return Array of x and y values
	 */
	public Vector2f applyTransform(Vector2f position) {
		return position;
//		position.x = (position.x - entity.transform.getGlobalX()) * getZoom() + getCenterX();
//		position.y = (position.y - entity.transform.getGlobalY()) * getZoom() + getCenterY();
//
//		return position;
	}
	
	public Vector2f screenToWorldPosition(Vector2i screenPosition) {
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
	
	public Matrix4f getViewMatrix() {
		Transform transform = getTransform();
		return viewMatrix.identity()
			.rotate((float)Math.toRadians(transform.rotation.x), new Vector3f(1, 0, 0))
			.rotate((float)Math.toRadians(transform.rotation.y), new Vector3f(0, 1, 0))
			.translate(-transform.position.x, -transform.position.y, -transform.position.z);
	}
	
	public Matrix4f getProjectionMatrix() {
		if (orthographic) {
			float right = width / 2f;
			float top = height / 2f;
			projectionMatrix.setOrtho(-right, right, -top, top, nearClipPlane, farClipPlane);
		} else {
			projectionMatrix.setPerspective((float)Math.toRadians(fieldOfView), width / height, nearClipPlane, farClipPlane);
		}
		return projectionMatrix;
	}
}
