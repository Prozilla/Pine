package dev.prozilla.pine.core.object.canvas;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * Represents the user interface.
 * This game object holds all elements of the user interface.
 * Wrapper object for the {@link CanvasRenderer} component.
 */
public class Canvas extends GameObject {
	
	protected final CanvasRenderer canvasRenderer;
	
	public Canvas(Game game) {
		super(game);
		
		canvasRenderer = new CanvasRenderer();
		addComponent(canvasRenderer);
	}
	
	/**
	 * Renders this canvas to the screen.
	 */
	@Override
	public void render(Renderer renderer) {
		canvasRenderer.beforeRender(renderer);
		super.render(renderer);
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
