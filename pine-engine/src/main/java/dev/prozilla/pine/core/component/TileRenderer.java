package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.system.resource.Texture;

import java.awt.*;

/**
 * A component for rendering 2D square tiles in the world.
 */
public class TileRenderer extends SpriteRenderer {
	
	public Point coordinate;
	/** Width and height of the tile, in pixels. */
	public int size;
	/** Horizontal offset for this sprite, in pixels. */
	public float offsetX;
	/** Vertical offset for this sprite, in pixels. */
	public float offsetY;
	
	public TileRenderer(Texture texture) {
		this(texture, new Point());
	}
	
	public TileRenderer(Texture texture, Point coordinate) {
		this(texture, coordinate, texture.getWidth());
	}
	
	public TileRenderer(Texture texture, Point coordinate, int size) {
		super("TileRenderer", texture);
		
		this.coordinate = coordinate;
		this.size = size;
		
		offsetX = 0;
		offsetY = 0;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		gameObject.x = coordinate.x * size + offsetX;
		gameObject.y = coordinate.y * size + offsetY;
	}
}