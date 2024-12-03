package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.SpriteRenderer;

/**
 * Represents a 2D sprite inside the world.
 */
public class Sprite extends Entity {
	
	protected final SpriteRenderer spriteRenderer;
	
	public Sprite(World world, String texturePath) {
		this(world, ResourcePool.loadTexture(texturePath));
	}
	
	public Sprite(World world, Texture texture) {
		super(world);
		
		spriteRenderer = new SpriteRenderer(texture);
		addComponent(spriteRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Sprite");
	}
}
