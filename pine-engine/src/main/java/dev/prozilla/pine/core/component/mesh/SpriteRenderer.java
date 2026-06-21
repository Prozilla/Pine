package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Sprite;

public class SpriteRenderer extends MeshRenderer<Sprite> {
	
	public SpriteRenderer(String texturePath) {
		this(texturePath, null);
	}
	
	public SpriteRenderer(TextureAsset texture) {
		this(texture, null);
	}
	
	public SpriteRenderer(String texturePath, Color color) {
		this(AssetPools.textures.load(texturePath), color);
	}
	
	public SpriteRenderer(TextureAsset texture, Color color) {
		this(new Sprite(texture), texture, color);
	}
	
	public SpriteRenderer(Sprite sprite) {
		super(sprite);
	}
	
	public SpriteRenderer(Sprite sprite, Color color) {
		super(sprite, color);
	}
	
	public SpriteRenderer(Sprite sprite, TextureAsset texture) {
		super(sprite, texture);
	}
	
	public SpriteRenderer(Sprite sprite, TextureAsset texture, Color color) {
		super(sprite, texture, color);
	}
	
}
