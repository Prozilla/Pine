package dev.prozilla.pine.examples.chat.entity;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.examples.chat.Chat;

public class ButtonPrefab extends TextButtonPrefab {
	
	public ButtonPrefab(String text) {
		super(text);
		setPadding(new DualDimension(40, 12));
		setFont(Chat.FONT);
		setBackgroundColor((Color)null);
		setBorderImage(AssetPools.textures.load("images/png/Default/Panel/panel-009.png"), new Vector4f(0.25f, 0.25f, 0.25f, 0.25f), true);
		setBorder(new Dimension(12));
		setColor(Chat.BACKGROUND_COLOR_B);
		setBorderColor(Chat.FOREGROUND_COLOR_A);
	}
	
}
