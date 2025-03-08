package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TooltipRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystemBase;

import java.util.Comparator;

/**
 * Handles clicks on canvas elements.
 */
public class RectInputHandler extends InputSystemBase {
	
	public RectInputHandler() {
		super(RectTransform.class);
		setExcludedComponentTypes(TooltipRenderer.class);
	}
	
	@Override
	public void input(float deltaTime) {
		Input input = getInput();
		Vector2i cursor = input.getCursor();
		
		forEachReverse(chunk -> {
			Entity entity = chunk.getEntity();
			RectTransform rect = chunk.getComponent(RectTransform.class);
			
			rect.cursorHit = false;
			
			if (!rect.passThrough && !rect.isInTooltip() && !input.isCursorBlocked()) {
				int canvasHeight = rect.getCanvas().getHeight();
				if (cursor != null && RectTransform.isInsideRect(new Vector2i(cursor.x, canvasHeight - cursor.y), rect.currentPosition, rect.currentSize)) {
					rect.cursorHit = true;
					input.blockCursor(entity);
				}
			}
			
			if (rect.tooltip != null) {
				rect.tooltip.setActive(rect.cursorHit);
			}
		});
	}
	
	@Override
	public void sort() {
		sort(Comparator.comparingInt(a -> a.getTransform().getDepthIndex()));
	}
}
