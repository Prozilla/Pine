package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Input;

public class TextInputNode extends Component {
	
	public int cursorPosition;
	public Input.TextListener textListener;
	public Type type;
	public TextNode textNode;
	
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
	
	public String getText() {
		return textNode.text;
	}
	
	public void clearText() {
		setText("");
	}
	
	public void setText(String text) {
		if (textNode != null && textNode.setText(text)) {
			updateCursor();
		}
	}
	
	public void updateCursor() {
		if (textNode == null || textNode.text == null) {
			cursorPosition = 0;
		} else {
			cursorPosition = Math.clamp(cursorPosition, 0, textNode.text.length());
		}
	}
	
}
