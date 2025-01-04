package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering rectangular shapes on the canvas.
 */
public class RectRenderer extends Component {
	
	public Color color;
	
	public RectRenderer() {
		this(Color.white());
	}
	
	public RectRenderer(Color color) {
		this.color = color;
	}
	
	@Override
	public String getName() {
		return "RectRenderer";
	}
}
