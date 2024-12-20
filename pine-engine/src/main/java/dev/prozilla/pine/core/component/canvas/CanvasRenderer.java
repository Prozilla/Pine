package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component {
	
	public Vector2i size;
	
	public CanvasRenderer() {
		size = new Vector2i();
	}
	
	@Override
	public String getName() {
		return "CanvasRenderer";
	}
	
	public int getWidth() {
		return size.x;
	}
	
	public int getHeight() {
		return size.y;
	}
}
