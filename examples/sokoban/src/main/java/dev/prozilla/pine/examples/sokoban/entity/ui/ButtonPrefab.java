package dev.prozilla.pine.examples.sokoban.entity.ui;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class ButtonPrefab extends TextButtonPrefab {

	public ButtonPrefab(String text) {
		super(text);
		setPadding(new DualDimension(40, 12));
		setFont(GameManager.instance.font);
		setBackgroundColor(Color.black());
		setColor(Color.white());
	}

}
