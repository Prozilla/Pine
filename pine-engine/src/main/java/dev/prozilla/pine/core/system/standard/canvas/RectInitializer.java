package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class RectInitializer extends InitSystem {
	
	public RectInitializer() {
		super(RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Entity entity = chunk.getEntity();
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		CanvasGroup group = entity.getComponentInParent(CanvasGroup.class, false);
		if (group != null) {
			group.getChildComponents();
		}
		
		// Make sure rect is correct positioned if activated after the RectUpdater system
		// TO DO: optimize this so it only gets triggered if a rect was activated after the RectUpdater system
		RectUpdater.updateRect(rect);
	}
}
