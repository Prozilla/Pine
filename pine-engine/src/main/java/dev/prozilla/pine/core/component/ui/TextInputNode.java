package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Input;

public class TextInputNode extends Component {
	
	public int cursorPosition;
	public Input.TextListener textListener;
	public Type type;
	
	public enum Type {
		TEXT,
		NUMBER
	}
	
	public TextInputNode() {
		type = Type.TEXT;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (textListener != null) {
			getInput().removeTextListener(textListener);
		}
	}
}
