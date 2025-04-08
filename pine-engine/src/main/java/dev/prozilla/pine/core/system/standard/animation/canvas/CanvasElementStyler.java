package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasElementStyle;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class CanvasElementStyler extends UpdateSystem {
	
	public CanvasElementStyler() {
		super( RectTransform.class, CanvasElementStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		RectTransform rect = chunk.getComponent(RectTransform.class);
		CanvasElementStyle canvasElementStyle = chunk.getComponent(CanvasElementStyle.class);
		
		if (canvasElementStyle.getColorProperty() != null) {
			if (rect.color == null) {
				rect.color = canvasElementStyle.getColorProperty().getValue();
			} else {
				canvasElementStyle.getColorProperty().transmit(rect.color);
			}
		}
		if (canvasElementStyle.getBackgroundColorProperty() != null) {
			if (rect.backgroundColor == null) {
				rect.backgroundColor = canvasElementStyle.getBackgroundColorProperty().getValue();
			} else {
				canvasElementStyle.getBackgroundColorProperty().transmit(rect.backgroundColor);
			}
		}
		if (canvasElementStyle.getSizeProperty() != null) {
			rect.size = canvasElementStyle.getSizeProperty().getValue();
		}
		if (canvasElementStyle.getPaddingProperty() != null) {
			rect.padding = canvasElementStyle.getPaddingProperty().getValue();
		}
		if (canvasElementStyle.getPositionProperty() != null) {
			rect.position = canvasElementStyle.getPositionProperty().getValue();
		}
	}
}
