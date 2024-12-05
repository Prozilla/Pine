package dev.prozilla.pine.core.entity.camera;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.common.system.resource.Color;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glClearColor;

public class Camera extends Entity {
	
	public CameraData cameraData;
	
	/**
	 * Creates a camera object with a black background color
	 */
	public Camera(World world) {
		this(world, Color.BLACK);
	}
	
	public Camera(World world, Color backgroundColor) {
		super(world);
		
		cameraData = new CameraData(backgroundColor);
		addComponent(cameraData);
	}
	
	@Override
	public String getName() {
		return getName("Camera");
	}
	
	public float getWidth() {
		return cameraData.width;
	}
	
	public float getHeight() {
		return cameraData.height;
	}
	
	public void setBackgroundColor(Color color) {
		cameraData.backgroundColor = color;
	}
	
	/**
	 * Zooms the camera by a factor.
	 * @param factor Zoom factor
	 */
	public void multiplyZoom(float factor) {
		cameraData.zoomFactor *= factor;
	}
	
	/**
	 * Zooms the camera in or out based on a zoom value.
	 * Use a positive value to zoom in and a negative value to zoom out.
	 * @param zoom Zoom value
	 */
	public void zoomIn(float zoom) {
		cameraData.zoomFactor = cameraData.zoomFactor + zoom;
	}
	
	/**
	 * Returns the scale of the camera's viewport.
	 * @return Scale
	 */
	public float getScale() {
		return Math.max(cameraData.width, cameraData.height) / 800f;
	}
	
	/**
	 * Returns the zoom value multiplied by the scale of the viewport.
	 * @return Zoom value
	 */
	public float getZoom() {
		return cameraData.zoomFactor * cameraData.zoomFactor * cameraData.zoomFactor * getScale();
	}
	
	/**
	 * Returns the horizontal center of this camera.
	 * @return X value
	 */
	public float getCenterX() {
		return cameraData.width / 2f;
	}
	
	/**
	 * Returns the vertical center of this camera.
	 * @return Y value
	 */
	public float getCenterY() {
		return cameraData.height / 2f;
	}
	
	/**
	 * Applies the camera's transformation to an x and y value.
	 * @param x X value
	 * @param y Y value
	 * @return Array of x and y values
	 */
	public float[] applyTransform(float x, float y) {
		x = (x - transform.getGlobalX()) * getZoom() + getCenterX();
		y = (y - transform.getGlobalY()) * getZoom() + getCenterY();
		
		return new float[]{ x, y };
	}
	
	public float[] screenToWorldPosition(Point screenPosition) {
		if (screenPosition == null) {
			return new float[]{ 0, 0 };
		}
		
		return screenToWorldPosition(screenPosition.x, screenPosition.y);
	}
	
	public float[] screenToWorldPosition(int screenX, int screenY) {
		float x = ((float)screenX - getCenterX()) / getZoom() + transform.getGlobalX();
		float y = (getCenterY() - (float)screenY) / getZoom() + transform.getGlobalY();
		
		return new float[]{ x, y };
	}
}
