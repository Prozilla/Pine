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
			
			tooltipRenderer.cursorX.setValue(Math.round(cursorPosition.x * (1f - (rect.anchor.x * 2))));
			tooltipRenderer.cursorY.setValue(Math.round(cursorPosition.y * ((rect.anchor.y * 2) - 1f)));
			tooltipRenderer.baseX.setValue(Math.round(windowWidth * (rect.anchor.x)));
			tooltipRenderer.baseY.setValue(Math.round(windowHeight * (1f - rect.anchor.y)));
		});
	}
}
