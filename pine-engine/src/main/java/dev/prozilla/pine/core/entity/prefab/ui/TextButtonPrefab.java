package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.ButtonNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for text buttons in the UI.
 */
@Components({ ButtonNode.class, TextNode.class, Node.class, Transform.class })
public class TextButtonPrefab extends TextPrefab {
	
	protected ButtonNode.ClickCallback clickCallback;
	
	public TextButtonPrefab() {
		this(null);
	}
	
	public TextButtonPrefab(String text) {
		super(text);
		setName("TextButton");
		addClass("button");
		setBackgroundColor(Color.white());
		setColor(Color.black());
	}
	
	public void setClickCallback(ButtonNode.ClickCallback callback) {
		clickCallback = callback;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ButtonNode buttonNode = entity.addComponent(new ButtonNode());
		
		if (clickCallback != null) {
			buttonNode.clickCallback = clickCallback;
		}
	}
}
