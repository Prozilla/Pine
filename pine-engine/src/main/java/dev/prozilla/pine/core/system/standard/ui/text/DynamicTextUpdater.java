package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.DynamicText;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public final class DynamicTextUpdater extends UpdateSystem {
	
	public DynamicTextUpdater() {
		super(TextNode.class, DynamicText.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		DynamicText dynamicText = chunk.getComponent(DynamicText.class);
		
		String newText = dynamicText.textProperty.getValue();
		
		if (newText == null || !newText.equals(textNode.text)) {
			textNode.setText(newText);
		}
	}
}
