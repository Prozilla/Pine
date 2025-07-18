package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;

public class RectRenderer extends Component {
	
	public Vector2f size;
	public final Color color;
	
	public static final Color DEFAULT_COLOR = Color.white();
	
	public RectRenderer(Vector2f size) {
		this(size, DEFAULT_COLOR.clone());
	}
	
	public RectRenderer(Vector2f size, Color color) {
		this.size = size;
		this.color = color;
	}
	
}
