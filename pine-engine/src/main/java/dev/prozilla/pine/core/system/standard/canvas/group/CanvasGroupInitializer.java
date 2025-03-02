package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.EntityEvent;
import dev.prozilla.pine.core.system.init.InitSystem;

/**
 * Initializes canvas groups by fetching their children.
 */
public class CanvasGroupInitializer extends InitSystem {
	
	public CanvasGroupInitializer() {
		super(CanvasGroup.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		CanvasGroup canvasGroup = chunk.getComponent(CanvasGroup.class);
		canvasGroup.getChildComponents();
		
		// Fetch child rects whenever children are added/removed
		chunk.getEntity().addListener(EntityEvent.CHILDREN_UPDATE, canvasGroup::getChildComponents);
	}
}
