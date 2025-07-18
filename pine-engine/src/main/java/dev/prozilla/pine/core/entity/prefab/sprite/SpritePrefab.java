package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for 2D sprite entities.
 */
@Components({ SpriteRenderer.class, Transform.class })
public class SpritePrefab extends Prefab {
	
	protected TextureBase texture;
	protected Color color;
	protected Vector2f scale;
	
	protected boolean cropToRegion;
	protected Vector2f regionOffset;
	protected Vector2f regionSize;
	
	public SpritePrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public SpritePrefab(TextureBase texture) {
		this.texture = Checks.isNotNull(texture, "texture");
		
		color = null;
		scale = null;
		
		cropToRegion = false;
		regionOffset = new Vector2f();
		regionSize = new Vector2f(texture.getWidth(), texture.getHeight());
		
		setName("Sprite");
	}
	
	/**
	 * Crops the sprite to a given region.
	 */
	public void setRegion(float x, float y, float width, float height) {
		cropToRegion = true;
		regionOffset.x = x;
		regionOffset.y = y;
		regionSize.x = width;
		regionSize.y = height;
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		SpriteRenderer spriteRenderer = color != null ? new SpriteRenderer(texture, color) : new SpriteRenderer(texture);
		
		if (scale != null) {
			spriteRenderer.scale = scale.clone();
		}
		if (cropToRegion) {
			spriteRenderer.setRegion(regionOffset, regionSize);
		}
		
		entity.addComponent(spriteRenderer);
	}
}
