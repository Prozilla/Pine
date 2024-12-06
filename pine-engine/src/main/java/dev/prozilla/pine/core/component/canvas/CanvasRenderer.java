package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component {
	
	public int width;
	public int height;
	
	@Override
	public String getName() {
		return "CanvasRenderer";
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
