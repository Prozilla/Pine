package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

public class SettingsPrefab extends LayoutPrefab {
	
	public SettingsPrefab(Font font) {
		setBackgroundColor(Color.white());
		setSize(DualDimension.fullscreen());
		setAlignment(EdgeAlignment.CENTER);
		setDistribution(LayoutNode.Distribution.CENTER);
		setAbsolutePosition(true);
		setActive(false);
		
		TextPrefab titlePrefab = new TextPrefab("Options", Color.black());
		titlePrefab.setFont(font);
		
		addChild(titlePrefab);
	}
	
}
