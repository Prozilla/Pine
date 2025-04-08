package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for text buttons in the UI.
 */
public class TextButtonPrefab extends TextPrefab {
	
	protected ButtonData.ClickCallback clickCallback;
	
	public TextButtonPrefab() {
		this(null);
	}
	
	public TextButtonPrefab(String text) {
		super(text);
		setName("TextButton");
		setBackgroundColor(Color.white());
		setColor(Color.black());
	}
	
	public void setClickCallback(ButtonData.ClickCallback callback) {
		clickCallback = callback;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ButtonData buttonData = entity.addComponent(new ButtonData());
		
		if (clickCallback != null) {
			buttonData.clickCallback = clickCallback;
		}
	}
}
