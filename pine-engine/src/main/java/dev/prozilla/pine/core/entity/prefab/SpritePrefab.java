package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;

import java.util.Objects;

/**
 * Prefab for 2D sprite entities.
 */
public class SpritePrefab extends Prefab {
	
	protected Texture texture;
	
	protected boolean cropToRegion;
	protected float regionX;
	protected float regionY;
	protected float regionWidth;
	protected float regionHeight;
	
	public SpritePrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public SpritePrefab(Texture texture) {
		Objects.requireNonNull(texture, "Texture must not be null.");
		
		this.texture = texture;
		cropToRegion = false;
		setName("Sprite");
	}
	
	/**
	 * Crops the sprite to a given region.
	 */
	public void setRegion(float x, float y, float width, float height) {
		cropToRegion = true;
		regionX = x;
		regionY = y;
		regionWidth = width;
		regionHeight = height;
	}
	
	@Override
	protected void apply(Entity entity) {
		SpriteRenderer spriteRenderer = entity.addComponent(new SpriteRenderer(texture));
		
		if (cropToRegion) {
			spriteRenderer.setRegion(regionX, regionY, regionWidth, regionHeight);
		}
	}
}
