package dev.prozilla.pine.core.entity.camera;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.common.system.resource.Color;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glClearColor;

public class Camera extends Entity {
	
	protected float zoomFactor;
	
	protected float width;
	protected float height;
	
	Color backgroundColor;
	
	/**
	 * Creates a camera object with a black background color
	 */
	public Camera(World world) {
		this(world, Color.BLACK);
	}
	
	public Camera(World world, Color backgroundColor) {
		super(world);
		
		this.backgroundColor = backgroundColor;
		
		zoomFactor = 1f;
	}
	
	@Override
	public void init(long window) {
		super.init(window);
		renderBackgroundColor(backgroundColor);
		
		setSize();
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		setSize();
	}
	
	@Override
	public String getName() {
		return getName("Camera");
	}
	
	protected void setSize() {
		width = getWindow().getWidth();
		height = getWindow().getHeight();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	public void renderBackgroundColor(Color color) {
		if (!Application.initializedOpenGL) {
			throw new RuntimeException("Can't render background color before initialization");
		}
		
		glClearColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
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
		return this.width / 2f;
	}
	
	/**
	 * Returns the vertical center of this camera.
	 * @return Y value
	 */
	public float getCenterY() {
		return this.height / 2f;
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
