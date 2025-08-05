package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shape.Circle;

public class CircleRenderer extends ShapeRenderer<Circle> {
	
	public CircleRenderer(Circle shape) {
		super(shape);
	}
	
	public CircleRenderer(Circle shape, Color color) {
		super(shape, color);
	}
	
	public CircleRenderer(Circle shape, TextureBase texture) {
		super(shape, texture);
	}
	
	public CircleRenderer(Circle shape, TextureBase texture, Color color) {
		super(shape, texture, color);
	}
	
}
