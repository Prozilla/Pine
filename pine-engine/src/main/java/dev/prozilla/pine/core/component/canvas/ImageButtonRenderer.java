package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering buttons with images on the canvas.
 */
public class ImageButtonRenderer extends Component {
	
	public Color hoverColor;
	public Color backgroundHoverColor;
	public Color backgroundColor;
	
	public Vector2i padding;
	
	public Callback clickCallback;
	
	public boolean isHovering;
	
	public ImageButtonRenderer() {
		this(Color.WHITE);
	}
	
	public ImageButtonRenderer(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		if (backgroundColor != null) {
			backgroundHoverColor = backgroundColor.clone();
			backgroundHoverColor.setAlpha(backgroundHoverColor.getAlpha() * 0.75f);
		}
		
		padding = new Vector2i();
		isHovering = false;
	}
	
	@Override
	public String getName() {
		return "ImageButtonRenderer";
	}
}
