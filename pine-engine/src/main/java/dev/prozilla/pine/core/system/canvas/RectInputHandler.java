package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.system.InputSystem;

import java.awt.*;

public class RectInputHandler extends InputSystem {
	
	public RectInputHandler() {
		super(RectTransform.class);
	}
	
	@Override
	public void input(float deltaTime) {
		forEach(match -> {
			Entity entity = match.getEntity();
			RectTransform rect = match.getComponent(RectTransform.class);
			
			if (!rect.passThrough) {
				Point cursor = getInput().getCursor();
				int canvasHeight = rect.getCanvas().getHeight();
				if (cursor != null && rect.isInside(cursor.x, canvasHeight - cursor.y, rect.x, rect.y, rect.width, rect.height)) {
					getInput().blockCursor(entity);
				}
			}
		});
	}
}
