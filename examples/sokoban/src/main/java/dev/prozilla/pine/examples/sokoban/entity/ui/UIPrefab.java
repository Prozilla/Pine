package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;

public class UIPrefab extends NodeRootPrefab {
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addChild(new HUDPrefab());
	}
}
