package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component {
	
	private int width;
	private int height;
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		width = getWindow().getWidth();
		height = getWindow().getHeight();
	}
	
	@Override
	public String getName() {
		return "CanvasRenderer";
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
