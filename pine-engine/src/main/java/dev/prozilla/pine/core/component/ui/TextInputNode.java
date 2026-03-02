package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.observable.ObservableStringProperty;
import dev.prozilla.pine.common.property.observable.SimpleObservableStringProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Input;

public class TextInputNode extends Component {
	
	public int cursorPosition;
	public int selection;
	public Input.TextListener textListener;
	public Type type;
	public TextNode textNode;
	private ObservableStringProperty textProperty;
	
	public enum Type {
		TEXT,
		NUMBER {
			@Override
			public boolean isValid(String input) {
				return MathUtils.isValidInteger(input);
			}
		};
		
		public boolean isValid(String input) {
			return true;
		}
		
	}
	
	public TextInputNode() {
		this(new SimpleObservableStringProperty(""));
	}
	
	public TextInputNode(ObservableStringProperty textProperty) {
		type = Type.TEXT;
		setTextProperty(textProperty);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (textListener != null) {
			getInput().removeTextListener(textListener);
		}
	}
	
	public String getText() {
		return textProperty.getValue();
	}
	
	public void clearText() {
		setText("");
	}
	
	public void setText(String text) {
		textProperty.setValue(text);
	}
	
	public ObservableStringProperty getTextProperty() {
		return textProperty;
	}
	
	public void setTextProperty(ObservableStringProperty textProperty) {
		Checks.isNotNull(textProperty, "textProperty");
		if (this.textProperty != null) {
			this.textProperty.removeObserver(this::handleTextChange);
		}
		this.textProperty = textProperty;
		textProperty.addObserver(this::handleTextChange);
	}
	
	private void handleTextChange(String text) {
		if (textNode != null && text != null) {
			textNode.setText(text);
		}
		updateCursor();
	}
	
	public boolean deleteSelection() {
		if (!hasSelection()) {
			return false;
		}
		
		cursorPosition = getSelectionStart();
		textProperty.buildValue((stringBuilder) -> stringBuilder.delete(cursorPosition, cursorPosition + Math.abs(selection)));
		clearSelection();
		return true;
	}
	
	public void deleteText(boolean inFront) {
		if (deleteSelection()) {
			return;
		}
		
		// Check if there are any chars to delete
		boolean canDelete = inFront ? !isCursorAtStart() : !isCursorAtEnd();
		if (!canDelete) {
			return;
		}
		
		if (inFront) {
			cursorPosition--;
		}
		textProperty.buildValue((stringBuilder) -> stringBuilder.deleteCharAt(cursorPosition));
	}
	
	public int getSelectionStart() {
		return Math.min(cursorPosition, cursorPosition + selection);
	}
	
	public int getSelectionEnd() {
		return Math.max(cursorPosition, cursorPosition + selection);
	}
	
	public boolean isCursorAtStart() {
		return cursorPosition <= 0;
	}
	
	public boolean isCursorAtEnd() {
		return cursorPosition >= textProperty.getLength();
	}
	
	public boolean hasSelection() {
		return selection != 0;
	}
	
	public void moveCursorLeft() {
		if (!hasSelection()) {
			cursorPosition--;
		} else {
			cursorPosition = getSelectionStart();
		}
		clearSelection();
		updateCursor();
	}
	
	public void moveCursorRight() {
		if (!hasSelection()) {
			cursorPosition++;
		} else {
			cursorPosition = getSelectionEnd();
		}
		clearSelection();
		updateCursor();
	}
	
	public void moveCursorToStart() {
		cursorPosition = 0;
		clearSelection();
		updateCursor();
	}
	
	public void moveCursorToEnd() {
		cursorPosition = textProperty.getLength();
		clearSelection();
		updateCursor();
	}
	
	public void clearSelection() {
		selection = 0;
	}
	
	public void expandSelectionLeft() {
		if (isCursorAtStart()) {
			return;
		}
		cursorPosition--;
		selection++;
		updateCursor();
	}
	
	public void expandSelectionRight() {
		if (isCursorAtEnd()) {
			return;
		}
		cursorPosition++;
		selection--;
		updateCursor();
	}
	
	public void updateCursor() {
		cursorPosition = MathUtils.clamp(cursorPosition, 0, textProperty.getLength());
		selection = MathUtils.clamp(cursorPosition + selection, 0, textProperty.getLength()) - cursorPosition;
	}
	
}
