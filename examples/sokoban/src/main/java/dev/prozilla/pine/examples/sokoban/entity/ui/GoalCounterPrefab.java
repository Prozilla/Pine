package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.core.entity.prefab.ui.ImagePrefab;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.component.CrateData;

public class GoalCounterPrefab extends LayoutPrefab {
	
	public GoalCounterPrefab(StyleSheet styleSheet, int crateType) {
		setName("GoalCounter");
		addClass("goal-counter");
		addStyleSheet(styleSheet);
		setAbsolutePosition(true);
		
		TextPrefab textPrefab = new TextPrefab("0/0");
		textPrefab.setFont(GameManager.instance.font);
		textPrefab.setText(() -> GameManager.instance.completedCrates + "/" + GameManager.instance.getCrateCount());
		
		ImagePrefab imagePrefab = new ImagePrefab(() -> CrateData.getNormalSprite(GameManager.instance.level.crateType));
		imagePrefab.addClass("goal-counter-image");
		imagePrefab.addStyleSheet(styleSheet);

		addChildren(textPrefab, imagePrefab);
	}
	
}
