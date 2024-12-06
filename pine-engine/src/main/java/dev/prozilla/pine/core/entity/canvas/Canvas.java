package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * Represents the user interface.
 * This game object holds all elements of the user interface.
 * Wrapper object for the {@link CanvasRenderer} component.
 */
public class Canvas extends Entity {
	
	protected final CanvasRenderer canvasRenderer;
	
	public Canvas(World world) {
		super(world);
		
		canvasRenderer = new CanvasRenderer();
		addComponent(canvasRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Canvas");
	}
	
	/**
	 * Returns the width of this canvas.
	 * @return Width of this canvas
	 */
	public int getWidth() {
		return canvasRenderer.getWidth();
	}
	
	/**
	 * Returns the height of this canvas.
	 * @return Height of this canvas
	 */
	public int getHeight() {
		return canvasRenderer.getHeight();
	}
}
