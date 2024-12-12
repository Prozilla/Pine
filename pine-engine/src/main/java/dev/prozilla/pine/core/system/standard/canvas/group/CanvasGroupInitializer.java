package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;

/**
 * Initializes canvas groups by fetching their children.
 */
public class CanvasGroupInitializer extends InitSystem {
	
	public CanvasGroupInitializer() {
		super(CanvasGroup.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		canvasGroup.getChildComponents();
	}
}
