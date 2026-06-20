package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.core.entity.EntityQuery;

public abstract class SceneRenderSystem extends RenderSystemBase {
	
	public SceneRenderSystem() {
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
