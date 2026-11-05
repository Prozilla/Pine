package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.examples.sokoban.level.Level;

public class UIPrefab extends NodeRootPrefab {
	
	public UIPrefab(StyleSheet styleSheet, Level level) {
		setName("UI");
		addChild(new HUDPrefab(styleSheet, level));
	}
	
}
