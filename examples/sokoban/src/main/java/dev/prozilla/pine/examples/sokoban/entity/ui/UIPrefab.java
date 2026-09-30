package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;

public class UIPrefab extends NodeRootPrefab {
	
	public UIPrefab(StyleSheet styleSheet) {
		setName("UI");
		addChild(new HUDPrefab(styleSheet));
	}
	
}
