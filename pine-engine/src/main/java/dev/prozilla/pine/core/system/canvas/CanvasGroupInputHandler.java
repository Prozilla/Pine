package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.system.InputSystem;

public class CanvasGroupInputHandler extends InputSystem {
	
	public CanvasGroupInputHandler() {
		super(CanvasGroup.class);
	}
	
	@Override
	public void input(float deltaTime) {
		forEach(match -> {
			CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
			
			canvasGroup.passThrough = (canvasGroup.backgroundColor == null);
		});
	}
}
