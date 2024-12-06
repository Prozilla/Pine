package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.system.InitSystem;

public class CanvasGroupInitializer extends InitSystem {
	
	public CanvasGroupInitializer() {
		super(CanvasGroup.class);
	}
	
	@Override
	public void init(long window) {
		forEach(match -> {
			CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
			
			canvasGroup.getChildComponents();
		});
	}
}
