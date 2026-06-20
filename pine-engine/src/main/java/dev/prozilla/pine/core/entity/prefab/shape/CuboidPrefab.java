package dev.prozilla.pine.core.entity.prefab.shape;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.shape.CuboidRenderer;
import dev.prozilla.pine.core.component.shape.ShapeRenderer;
import dev.prozilla.pine.core.rendering.shape.Cuboid;

public class CuboidPrefab extends ShapePrefab<Cuboid> {
	
	public CuboidPrefab(Cuboid shape) {
		super(shape);
	}
	
	public CuboidPrefab(Cuboid shape, Color color) {
		super(shape, color);
	}
	
	public CuboidPrefab(Cuboid shape, String texturePath) {
		super(shape, texturePath);
	}
	
	public CuboidPrefab(Cuboid shape, TextureAsset texture) {
		super(shape, texture);
	}
	
	public CuboidPrefab(Cuboid shape, String texturePath, Color color) {
		super(shape, texturePath, color);
	}
	
	public CuboidPrefab(Cuboid shape, TextureAsset texture, Color color) {
		super(shape, texture, color);
	}
	
	@Override
	protected ShapeRenderer<Cuboid> createRenderer(Cuboid shape, TextureAsset texture, Color color) {
		return new CuboidRenderer(shape, texture, color);
	}
	
}
