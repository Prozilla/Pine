package dev.prozilla.pine.core.system.standard.canvas.tooltip;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TooltipRenderer;
import dev.prozilla.pine.core.system.input.InputSystemBase;

public class TooltipInputHandler extends InputSystemBase {
	
	public TooltipInputHandler() {
		super(TooltipRenderer.class, RectTransform.class);
	}
	
	@Override
	public void input(float deltaTime) {
		Vector2i cursorPosition = getInput().getCursor(true);
		
		int windowWidth = application.getWindow().width;
		int windowHeight = application.getWindow().height;
		
		forEach((chunk) -> {
			RectTransform rect = chunk.getComponent(RectTransform.class);
			TooltipRenderer tooltipRenderer = chunk.getComponent(TooltipRenderer.class);
			
			tooltipRenderer.offsetX.setValue(cursorPosition.x);
			tooltipRenderer.offsetY.setValue(cursorPosition.y);
			tooltipRenderer.cursorX.setValue(Math.round(windowWidth * rect.anchor.x));
			tooltipRenderer.cursorY.setValue(Math.round(windowHeight * (1 - rect.anchor.y)));
		});
	}
}
