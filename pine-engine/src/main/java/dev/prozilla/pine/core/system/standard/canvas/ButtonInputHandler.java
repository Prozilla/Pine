package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public class ButtonInputHandler extends InputSystem {
	
	public ButtonInputHandler() {
		super(ButtonData.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		ButtonData buttonData = chunk.getComponent(ButtonData.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		buttonData.isHovering = rect.cursorHit;

		if (rect.cursorHit) {
			getInput().setCursorType(CursorType.HAND);

			if (buttonData.clickCallback != null && input.getMouseButtonDown(MouseButton.LEFT)) {
				buttonData.click();
			}
		}
	}
}
