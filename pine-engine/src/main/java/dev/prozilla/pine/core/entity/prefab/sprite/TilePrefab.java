package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

import java.awt.*;

/**
 * Prefab for 2D tiles.
 */
@Components({ TileRenderer.class, SpriteRenderer.class, Transform.class })
public class TilePrefab extends SpritePrefab {
	
	protected Point coordinate;
	protected int size;
	
	public TilePrefab(String texturePath) {
		this(texturePath, new Point());
	}
	
	public TilePrefab(Texture texture) {
		this(texture, new Point());
	}
	
	public TilePrefab(String texturePath, Point coordinate) {
		this(ResourcePool.loadTexture(texturePath), coordinate);
	}
	
	public TilePrefab(Texture texture, Point coordinate) {
		super(texture);
		
		this.coordinate = coordinate;
		size = texture.getWidth();
		setName("Grid");
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setCoordinate(int x, int y) {
		setCoordinate(new Point(x, y));
	}
	
	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}
	
	public Entity instantiate(World world, int x, int y) {
		return instantiate(world, new Point(x, y));
	}
	
	public Entity instantiate(World world, Point coordinate) {
		setCoordinate(coordinate);
		return super.instantiate(world);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new TileRenderer((Point)coordinate.clone(), size));
	}
}
