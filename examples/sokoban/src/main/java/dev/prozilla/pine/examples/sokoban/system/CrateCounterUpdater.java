package dev.prozilla.pine.examples.sokoban.system;

import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.GameManager;

public class CrateCounterUpdater extends UpdateSystem {
	
	public CrateCounterUpdater() {
		super(TextNode.class);
		setRequiredTag(EntityTag.GOAL_COUNTER);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		
		textNode.setText(GameManager.instance.completedCrates + "/" + GameManager.instance.totalCrates);
	}
}
