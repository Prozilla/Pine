package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component {
	
	private int width;
	private int height;
	
	public CanvasRenderer() {
		super("CanvasRenderer");
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		width = gameObject.game.window.getWidth();
		height = gameObject.game.window.getHeight();
	}
	
	public void beforeRender(Renderer renderer) {
		renderer.setScale(1f);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
