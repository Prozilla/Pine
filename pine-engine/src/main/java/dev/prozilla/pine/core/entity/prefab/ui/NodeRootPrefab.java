package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for user interfaces.
 */
@Components({ NodeRoot.class, Transform.class })
public class NodeRootPrefab extends Prefab {
	
	public NodeRootPrefab() {
		setName("NodeRoot");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new NodeRoot());
	}
}
