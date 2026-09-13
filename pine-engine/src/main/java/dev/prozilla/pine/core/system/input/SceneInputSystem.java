package dev.prozilla.pine.core.system.input;

import dev.prozilla.pine.core.entity.EntityQuery;

public abstract class SceneInputSystem extends InputSystemBase {
	
	public SceneInputSystem() {
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
