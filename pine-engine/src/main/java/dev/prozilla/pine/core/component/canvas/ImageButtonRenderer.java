package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering buttons with images on the canvas.
 */
public class ImageButtonRenderer extends Component {
	
	public Color backgroundColor;
	
	public DualDimension padding;
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.white();
	
	public ImageButtonRenderer() {
		this(DEFAULT_BACKGROUND_COLOR);
	}
	
	public ImageButtonRenderer(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		padding = new DualDimension();
	}
	
	@Override
	public String getName() {
		return "ImageButtonRenderer";
	}
}
