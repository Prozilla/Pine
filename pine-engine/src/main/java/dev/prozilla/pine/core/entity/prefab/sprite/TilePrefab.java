package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.MultiTileRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.system.standard.sprite.TileMover;

/**
 * Prefab for 2D tiles.
 */
@Components({ TileRenderer.class, SpriteRenderer.class, Transform.class })
public class TilePrefab extends SpritePrefab {
	
	protected Vector2i coordinate;
	protected int size;
	
	protected Vector2i dimensions;
	
	public TilePrefab(String texturePath) {
		this(texturePath, new Vector2i());
	}
	
	public TilePrefab(TextureBase texture) {
		this(texture, new Vector2i());
	}
	
	public TilePrefab(String texturePath, Vector2i coordinate) {
		this(AssetPools.textures.load(texturePath), coordinate);
	}
	
	public TilePrefab(TextureBase texture, Vector2i coordinate) {
		super(texture);
		
		this.coordinate = coordinate;
		size = texture.getWidth();
		setRegion(0, 0, size, size);
		setName("Tile");
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setCoordinate(int x, int y) {
		setCoordinate(new Vector2i(x, y));
	}
	
	public void setCoordinate(Vector2i coordinate) {
		this.coordinate = coordinate;
	}
	
	/**
	 * Converts this tile prefab to a multi-tile prefab with the given dimensions.
	 * @param dimensions The dimensions of the multi-tile
	 * @see MultiTileRenderer
	 */
	public void setDimensions(Vector2i dimensions) {
		this.dimensions = dimensions;
	}
	
	public Entity instantiate(World world, int x, int y) {
		return instantiate(world, new Vector2i(x, y));
	}
	
	public Entity instantiate(World world, Vector2i coordinate) {
		setCoordinate(coordinate);
		return super.instantiate(world);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TileRenderer tileRenderer = entity.addComponent(new TileRenderer(coordinate.clone(), size));
		
		if (dimensions != null) {
			entity.addComponent(new MultiTileRenderer(dimensions, tileRenderer));
		}
		
		TileMover.updateTilePosition(entity.transform, tileRenderer);
	}
}
