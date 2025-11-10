package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shape.Rect;

public class RectRenderer extends ShapeRenderer<Rect> {
	
	public RectRenderer(Rect shape) {
		super(shape);
	}
	
	public RectRenderer(Rect shape, Color color) {
		super(shape, color);
	}
	
	public RectRenderer(Rect shape, TextureAsset texture) {
		super(shape, texture);
	}
	
	public RectRenderer(Rect shape, TextureAsset texture, Color color) {
		super(shape, texture, color);
	}
	
}
