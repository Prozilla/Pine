package dev.prozilla.pine.core.system.standard.ui.dev;

import dev.prozilla.pine.core.component.ui.dev.DevConsole;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;

public class DevConsoleInputHandler extends InputSystem {
	
	public DevConsoleInputHandler() {
		super(DevConsole.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		DevConsole devConsole = chunk.getComponent(DevConsole.class);
		
		if (!devConsole.inputNode.isFocused()) {
			return;
		}
		
		if (input.getKeyDown(Key.ENTER)) {
			String text = devConsole.textNode.getText();
			devConsole.textNode.clearText();
			
			devConsole.log("> " + text);
			devConsole.execute(text);
		} else if (input.getKeyDown(Key.UP_ARROW)) {
			devConsole.history.selectNext();
			devConsole.textNode.setText(devConsole.history.getSelectedItem());
		} else if (input.getKeyDown(Key.DOWN_ARROW)) {
			devConsole.history.selectPrevious();
			devConsole.textNode.setText(devConsole.history.getSelectedItem());
		}
	}
	
}
