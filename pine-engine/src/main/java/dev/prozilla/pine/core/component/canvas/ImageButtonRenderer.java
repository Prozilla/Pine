package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering buttons with images on the canvas.
 */
public class ImageButtonRenderer extends Component {
	
	public Color hoverColor;
	public Color backgroundHoverColor;
	public Color backgroundColor;
	
	public DualDimension padding;
	
	public ImageButtonRenderer() {
		this(Color.white());
	}
	
	public ImageButtonRenderer(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		if (backgroundColor != null) {
			backgroundHoverColor = backgroundColor.clone();
			backgroundHoverColor.setAlpha(backgroundHoverColor.getAlpha() * 0.75f);
		}
		
		padding = new DualDimension();
	}
	
	@Override
	public String getName() {
		return "ImageButtonRenderer";
	}
}
