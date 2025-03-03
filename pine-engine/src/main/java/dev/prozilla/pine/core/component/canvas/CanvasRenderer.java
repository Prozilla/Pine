package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component implements CanvasContext {
	
	public Vector2i size;
	
	public CanvasRenderer() {
		size = new Vector2i();
	}
	
	@Override
	public String getName() {
		return "CanvasRenderer";
	}
	
	@Override
	public int getX() {
		return 0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return size.x;
	}
	
	@Override
	public int getHeight() {
		return size.y;
	}
}
