package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.core.entity.EntityQuery;

public abstract class SceneInitSystem extends InitSystemBase {
	
	public SceneInitSystem() {
		super();
	}
	
	@Override
	protected EntityQuery createQuery() {
		return null;
	}
	
	@Override
	public boolean shouldRun() {
		return true;
	}
}
