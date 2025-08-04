package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.shape.ShapePrefab;

public class BarPrefab extends ShapePrefab {
	
	protected int index;
	protected BarRect barRect;
	
	public BarPrefab() {
		this(new BarRect());
	}
	
	protected BarPrefab(BarRect barRect) {
		super(barRect, Color.white());
		index = 0;
		this.barRect = barRect;
	}
	
	public void setIndex(int index) {
		this.index = index;
		barRect = new BarRect();
		setShape(barRect);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.transform.position.y = -Main.HEIGHT / 2f;
		entity.addComponent(new BarData(index, barRect));
	}
	
}
