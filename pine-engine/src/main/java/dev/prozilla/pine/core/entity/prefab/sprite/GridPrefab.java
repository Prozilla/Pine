package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for 2D tile grids.
 */
public class GridPrefab extends Prefab {
	
	protected int size;
	
	public GridPrefab(int size) {
		this.size = size;
		setName("Grid");
	}
	
	@Override
	protected void apply(Entity entity) {
		entity.addComponent(new GridGroup(size));
	}
}
