package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.shape.Shape;

public class ShapeRenderer<S extends Shape> extends Component {
	
	public S shape;
	public TextureBase texture;
	public Color color;
	
	public ShapeRenderer(S shape) {
		this(shape, null, null);
	}
	
	public ShapeRenderer(S shape, Color color) {
		this(shape, null, color);
	}
	
	public ShapeRenderer(S shape, TextureBase texture) {
		this(shape, texture, null);
	}
	
	public ShapeRenderer(S shape, TextureBase texture, Color color) {
		this.shape = shape;
		this.texture = texture;
		this.color = color;
	}
	
	public S getShape() {
		return shape;
	}
	
}
