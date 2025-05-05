package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.property.SuppliedProperty;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.ImagePrefab;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class GoalCounterPrefab extends LayoutPrefab {
	
	public GoalCounterPrefab() {
		setName("GoalCounter");
		addClass("goal-counter");
		setStyleSheet("style/hud.css");
		setAbsolutePosition(true);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextPrefab textPrefab = new TextPrefab("0/0");
		textPrefab.setFont(GameManager.instance.font);
		textPrefab.setText(new SuppliedProperty<>(() -> GameManager.instance.completedCrates + "/" + GameManager.instance.totalCrates));
		entity.addChild(textPrefab);
		
		ImagePrefab imagePrefab = new ImagePrefab("images/crates/crate_02.png");
		imagePrefab.addClass("goal-counter-image");
		imagePrefab.setStyleSheet(styleSheet);
		entity.addChild(imagePrefab);
	}
}
