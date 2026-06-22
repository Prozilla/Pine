package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.mesh.RectPrefab;
import dev.prozilla.pine.core.rendering.mesh.Rect;

public class BarPrefab extends RectPrefab {
	
	protected int index;
	
	public BarPrefab() {
		super(new Rect(), Color.white());
		index = 0;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.transform.position.y = -Main.HEIGHT / 2f;
		entity.addComponent(new BarData(index));
		
		// Create new mesh for next bar
		setMesh(new Rect());
	}
	
}
