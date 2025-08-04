package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.shape.Shape;

public class ShapeRenderer extends Component {
	
	public Shape shape;
	public TextureBase texture;
	public Color color;
	
	public ShapeRenderer(Shape shape) {
		this(shape, null, null);
	}
	
	public ShapeRenderer(Shape shape, Color color) {
		this(shape, null, color);
	}
	
	public ShapeRenderer(Shape shape, TextureBase texture) {
		this(shape, texture, null);
	}
	
	public ShapeRenderer(Shape shape, TextureBase texture, Color color) {
		this.shape = shape;
		this.texture = texture;
		this.color = color;
	}
	
}
