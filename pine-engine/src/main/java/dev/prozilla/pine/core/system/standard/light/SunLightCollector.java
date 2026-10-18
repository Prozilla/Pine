package dev.prozilla.pine.core.system.standard.light;

import dev.prozilla.pine.common.util.collection.ListUtils;
import dev.prozilla.pine.core.component.light.SunLight;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

public class SunLightCollector extends UpdateSystemBase {
	
	public SunLightCollector() {
		super(SunLight.class);
	}
	
	@Override
	public void update(float deltaTime) {}
	
	@Override
	public boolean shouldRun() {
		return false;
	}
	
	public SunLight getSunLight() {
		EntityChunk chunk = ListUtils.getFirst(getChunks());
		if (chunk == null) {
			return null;
		}
		return chunk.getComponent(SunLight.class);
	}
	
}
