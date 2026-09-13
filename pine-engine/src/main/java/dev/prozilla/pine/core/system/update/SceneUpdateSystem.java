package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.entity.EntityQuery;

public abstract class SceneUpdateSystem extends UpdateSystemBase {
	
	public SceneUpdateSystem() {
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
