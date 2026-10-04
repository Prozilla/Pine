package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.material.UnlitMaterial;
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
		this(new Sprite(texture), new UnlitMaterial(texture, color));
	}
	
	public SpriteRenderer(Sprite sprite) {
		super(sprite);
	}
	
	public SpriteRenderer(Sprite sprite, Material<?> material) {
		super(sprite, material);
	}
	
}
