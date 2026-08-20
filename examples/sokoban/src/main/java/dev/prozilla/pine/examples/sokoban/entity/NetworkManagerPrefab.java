package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.examples.sokoban.component.NetworkManager;

public class NetworkManagerPrefab extends Prefab {
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new NetworkManager());
	}
}
