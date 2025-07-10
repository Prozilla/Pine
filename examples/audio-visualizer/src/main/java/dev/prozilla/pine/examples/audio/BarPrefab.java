package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.shape.RectPrefab;

public class BarPrefab extends RectPrefab {
	
	protected int index;
	
	public BarPrefab() {
		super(new Vector2f());
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
	}
	
}
