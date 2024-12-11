package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

import java.awt.*;

public class TextButtonInputHandler extends InputSystem {
	
	public TextButtonInputHandler() {
		super(TextButtonRenderer.class, TextRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, Input input, float deltaTime) {
		TextButtonRenderer textButtonRenderer = match.getComponent(TextButtonRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		textButtonRenderer.isHovering = rect.cursorHit;
		
		if (rect.cursorHit) {
			getInput().setCursorType(CursorType.HAND);
			
			if (textButtonRenderer.clickCallback != null && getInput().getMouseButtonDown(MouseButton.LEFT)) {
				textButtonRenderer.clickCallback.run();
			}
		}
	}
}
