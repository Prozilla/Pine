package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;

public class HUDPrefab extends LayoutPrefab {
	
	public HUDPrefab(StyleSheet styleSheet) {
		setName("HUD");
		addClass("hud");
		addStyleSheet(styleSheet);
		addChild(new GoalCounterPrefab(styleSheet));
	}
	
}
