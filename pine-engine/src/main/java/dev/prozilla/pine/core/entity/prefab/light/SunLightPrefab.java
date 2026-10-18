package dev.prozilla.pine.core.entity.prefab.light;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.light.SunLight;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

public class SunLightPrefab extends Prefab {
	
	protected Color color;
	protected float intensity;
	
	public SunLightPrefab(Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new SunLight(color, intensity));
	}
	
}
