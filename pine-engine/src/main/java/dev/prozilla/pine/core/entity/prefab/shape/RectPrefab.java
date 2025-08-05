package dev.prozilla.pine.core.entity.prefab.shape;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.component.shape.ShapeRenderer;
import dev.prozilla.pine.core.rendering.shape.Rect;

public class RectPrefab extends ShapePrefab<Rect> {
	
	public RectPrefab(Rect shape) {
		super(shape);
	}
	
	public RectPrefab(Rect shape, Color color) {
		super(shape, color);
	}
	
	public RectPrefab(Rect shape, String texturePath) {
		super(shape, texturePath);
	}
	
	public RectPrefab(Rect shape, TextureBase texture) {
		super(shape, texture);
	}
	
	public RectPrefab(Rect shape, String texturePath, Color color) {
		super(shape, texturePath, color);
	}
	
	public RectPrefab(Rect shape, TextureBase texture, Color color) {
		super(shape, texture, color);
	}
	
	@Override
	protected ShapeRenderer<Rect> createRenderer(Rect shape, TextureBase texture, Color color) {
		return new RectRenderer(shape, texture, color);
	}
	
}
