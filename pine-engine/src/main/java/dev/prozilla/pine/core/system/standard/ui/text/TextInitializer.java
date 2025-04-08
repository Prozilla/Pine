package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

/**
 * Initializes text elements by calculating their initial size.
 */
public class TextInitializer extends InitSystem {
	
	public TextInitializer() {
		super(TextNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		textNode.calculateSize();
	}
}
