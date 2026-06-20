package dev.prozilla.pine.core.component.shape;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shape.Cuboid;

public class CuboidRenderer extends ShapeRenderer<Cuboid> {
	
	public CuboidRenderer(Cuboid shape) {
		super(shape);
	}
	
	public CuboidRenderer(Cuboid shape, Color color) {
		super(shape, color);
	}
	
	public CuboidRenderer(Cuboid shape, TextureAsset texture) {
		super(shape, texture);
	}
	
	public CuboidRenderer(Cuboid shape, TextureAsset texture, Color color) {
		super(shape, texture, color);
	}
	
}
