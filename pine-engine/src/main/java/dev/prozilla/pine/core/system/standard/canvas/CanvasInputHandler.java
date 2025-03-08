package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

public class CanvasInputHandler extends InputSystem {
	
	public CanvasInputHandler() {
		super(CanvasRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		CanvasRenderer canvasRenderer = chunk.getComponent(CanvasRenderer.class);
		
		if (canvasRenderer.tooltip != null) {
			canvasRenderer.tooltip.setActive(canvasRenderer.tooltipActivator.cursorHit);
		}
	}
}
