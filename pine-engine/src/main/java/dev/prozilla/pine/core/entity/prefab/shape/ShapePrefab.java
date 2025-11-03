package dev.prozilla.pine.core.entity.prefab.shape;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.shape.ShapeRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.shape.Shape;

public class ShapePrefab<S extends Shape> extends Prefab {
	
	protected S shape;
	protected TextureAsset texture;
	protected Color color;
	
	public ShapePrefab(S shape) {
		this(shape, (TextureAsset)null);
	}
	
	public ShapePrefab(S shape, Color color) {
		this(shape, (TextureAsset)null, color);
	}
	
	public ShapePrefab(S shape, String texturePath) {
		this(shape, AssetPools.textures.load(texturePath), null);
	}
	
	public ShapePrefab(S shape, TextureAsset texture) {
		this(shape, texture, null);
	}
	
	public ShapePrefab(S shape, String texturePath, Color color) {
		this(shape, AssetPools.textures.load(texturePath), color);
	}
	
	public ShapePrefab(S shape, TextureAsset texture, Color color) {
		setName("Shape");
		
		this.shape = shape;
		this.texture = texture;
		this.color = color;
	}
	
	public void setShape(S shape) {
		this.shape = shape;
	}
	
	public void setTexture(TextureAsset texture) {
		this.texture = texture;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	protected ShapeRenderer<S> createRenderer(S shape, TextureAsset texture, Color color) {
		return new ShapeRenderer<>(shape, texture, color);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(createRenderer(shape, texture, Cloneable.cloneOf(color)));
	}
	
}
