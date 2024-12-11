package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;

public class RectInitializer extends InitSystem {
	
	public RectInitializer() {
		super(RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		Entity entity = match.getEntity();
		
		CanvasGroup group = entity.getComponentInParent(CanvasGroup.class, false);
		
		if (group != null) {
			group.getChildComponents();
		}
	}
}
