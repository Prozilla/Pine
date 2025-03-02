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
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.white();
	public static final Color DEFAULT_BACKGROUND_HOVER_COLOR = Color.white().setAlpha(0.75f);
	
	public ImageButtonRenderer() {
		this(DEFAULT_BACKGROUND_COLOR);
	}
	
	public ImageButtonRenderer(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		if (backgroundColor != null) {
			backgroundHoverColor = backgroundColor.clone();
			backgroundHoverColor.setAlpha(backgroundHoverColor.getAlpha() * DEFAULT_BACKGROUND_HOVER_COLOR.getAlpha());
		}
		
		padding = new DualDimension();
	}
	
	@Override
	public String getName() {
		return "ImageButtonRenderer";
	}
}
