package dev.prozilla.pine.core.system.standard.ui.dev;

import dev.prozilla.pine.core.component.ui.dev.DevConsoleData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;

public class DevConsoleInputHandler extends InputSystem {
	
	public DevConsoleInputHandler() {
		super(DevConsoleData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		DevConsoleData devConsoleData = chunk.getComponent(DevConsoleData.class);
		
		if (input.getKeyDown(Key.ENTER) && devConsoleData.inputNode.isFocused()) {
			String text = devConsoleData.textNode.getText();
			devConsoleData.textNode.clearText();
			
			devConsoleData.addLog("> " + text);
			
			String output = devConsoleData.handleInput(text);
			
			if (output != null) {
				devConsoleData.addLog(output);
			}
		}
	}
	
}
