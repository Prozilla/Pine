package dev.prozilla.pine.core.entity.prefab.sprite;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for 2D tile grids.
 */
@Components({ GridGroup.class, Transform.class })
public class GridPrefab extends Prefab {
	
	protected int size;
	
	public GridPrefab(int size) {
		this.size = size;
		setName("Grid");
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new GridGroup(size));
	}
}
