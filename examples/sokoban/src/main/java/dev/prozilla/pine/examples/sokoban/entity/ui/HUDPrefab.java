package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;

public class HUDPrefab extends LayoutPrefab {
	
	public HUDPrefab() {
		addClass("hud");
		setStyleSheet("style/hud.css");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addChild(new GoalCounterPrefab());
	}
}
