package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;

public class HUDPrefab extends LayoutPrefab {
	
	public HUDPrefab() {
		setName("HUD");
		addClass("hud");
		setStyleSheet("style/hud.css");
		addChild(new GoalCounterPrefab());
	}
	
}
