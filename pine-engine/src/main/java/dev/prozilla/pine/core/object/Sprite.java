package dev.prozilla.pine.core.object;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.SpriteRenderer;

/**
 * Represents a 2D sprite inside the world.
 */
public class Sprite extends GameObject {
	
	protected final SpriteRenderer spriteRenderer;
	
	public Sprite(Game game, String texturePath) {
		this(game, ResourcePool.loadTexture(texturePath));
	}
	
	public Sprite(Game game, Texture texture) {
		this(game, "Sprite", texture);
	}
	
	public Sprite(Game game, String name, String texturePath) {
		this(game, name, ResourcePool.loadTexture(texturePath));
	}
	
	public Sprite(Game game, String name, Texture texture) {
		super(game, name);
		
		spriteRenderer = new SpriteRenderer(texture);
		addComponent(spriteRenderer);
	}
}
