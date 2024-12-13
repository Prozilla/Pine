package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;

import java.awt.*;

/**
 * Prefab for 2D tiles.
 */
public class TilePrefab extends SpritePrefab {
	
	protected Point coordinate;
	protected int size;
	
	public TilePrefab(Texture texture, Point coordinate) {
		super(texture);
		
		this.coordinate = coordinate;
		size = texture.getWidth();
		setName("Grid");
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new TileRenderer(coordinate, size));
	}
}
