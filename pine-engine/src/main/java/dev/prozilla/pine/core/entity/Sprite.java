package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.SpriteRenderer;

/**
 * Represents a 2D sprite inside the world.
 */
public class Sprite extends Entity {
	
	protected final SpriteRenderer spriteRenderer;
	
	public Sprite(Game game, String texturePath) {
		this(game, ResourcePool.loadTexture(texturePath));
	}
	
	public Sprite(Game game, Texture texture) {
		super(game);
		
		spriteRenderer = new SpriteRenderer(texture);
		addComponent(spriteRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Sprite");
	}
}
