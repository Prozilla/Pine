package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.system.resource.Texture;

import java.awt.*;

/**
 * A component for rendering 2D square tiles in the world.
 */
public class TileRenderer extends SpriteRenderer {
	
	public Point coordinate;
	/** Width and height of the tile, in pixels. */
	public int size;
	
	public TileRenderer(Texture texture) {
		this(texture, new Point());
	}
	
	public TileRenderer(Texture texture, Point coordinate) {
		this(texture, coordinate, texture.getWidth());
	}
	
	public TileRenderer(Texture texture, Point coordinate, int size) {
		super(texture);
		
		this.coordinate = coordinate;
		this.size = size;
		
		offsetX = 0;
		offsetY = 0;
	}
	
	@Override
	public String getName() {
		return "TileRenderer";
	}
}
