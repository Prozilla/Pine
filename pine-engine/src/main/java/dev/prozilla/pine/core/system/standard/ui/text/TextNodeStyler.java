package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.component.ui.style.TextNodeStyle;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public final class TextNodeStyler extends UpdateSystem {
	
	public TextNodeStyler() {
		super( TextNode.class, TextNodeStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		TextNodeStyle textNodeStyle = chunk.getComponent(TextNodeStyle.class);
		
		if (textNodeStyle.getFontSizeProperty() != null) {
			textNode.setFontSize(textNodeStyle.getFontSizeProperty().getValue());
		}
		if (textNodeStyle.getFontFamilyProperty() != null) {
			textNode.setFont(textNodeStyle.getFont());
		}
	}
	
}
