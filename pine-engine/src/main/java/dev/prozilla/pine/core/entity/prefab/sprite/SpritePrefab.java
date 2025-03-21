package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

import java.util.Objects;

/**
 * Prefab for 2D sprite entities.
 */
@Components({ SpriteRenderer.class, Transform.class })
public class SpritePrefab extends Prefab {
	
	protected TextureBase texture;
	protected Color color;
	protected float scale;
	
	protected boolean cropToRegion;
	protected Vector2f regionOffset;
	protected Vector2f regionSize;
	
	public SpritePrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public SpritePrefab(TextureBase texture) {
		Objects.requireNonNull(texture, "Texture must not be null.");
		
		this.texture = texture;
		
		color = null;
		scale = 1;
		
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
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		SpriteRenderer spriteRenderer = color != null ? new SpriteRenderer(texture, color) : new SpriteRenderer(texture);
		spriteRenderer.scale = scale;
		
		if (cropToRegion) {
			spriteRenderer.setRegion(regionOffset, regionSize);
		}
		
		entity.addComponent(spriteRenderer);
	}
}
