package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.core.entity.prefab.ui.ImagePrefab;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class GoalCounterPrefab extends LayoutPrefab {
	
	public GoalCounterPrefab(StyleSheet styleSheet) {
		setName("GoalCounter");
		addClass("goal-counter");
		addStyleSheet(styleSheet);
		setAbsolutePosition(true);
		
		TextPrefab textPrefab = new TextPrefab("0/0");
		textPrefab.setFont(GameManager.instance.font);
		textPrefab.setText(() -> GameManager.instance.completedCrates + "/" + GameManager.instance.totalCrates);
		
		ImagePrefab imagePrefab = new ImagePrefab("images/crates/crate_02.png");
		imagePrefab.addClass("goal-counter-image");
		imagePrefab.addStyleSheet(styleSheet);

		addChildren(textPrefab, imagePrefab);
	}
	
}
