package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;

/**
 * A component for rendering buttons with text on the canvas.
 */
public class TextButtonRenderer extends Component {
	
	public Color hoverColor;
	public Color backgroundHoverColor;
	public Color backgroundColor;
	public Vector2i padding;
	public Callback clickCallback;
	public boolean isHovering;
	
	@FunctionalInterface
	public interface Callback {
		
		void invoke(Entity entity);
	}
	
	public TextButtonRenderer() {
		this(Color.WHITE.clone());
	}
	
	public TextButtonRenderer(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		if (backgroundColor != null) {
			backgroundHoverColor = backgroundColor.clone();
			backgroundHoverColor.setAlpha(backgroundHoverColor.getAlpha() * 0.75f);
		}
		
		padding = new Vector2i();
		isHovering = false;
	}
	
	public void click() {
		clickCallback.invoke(getEntity());
	}
	
	@Override
	public String getName() {
		return "TextButtonRenderer";
	}
}
