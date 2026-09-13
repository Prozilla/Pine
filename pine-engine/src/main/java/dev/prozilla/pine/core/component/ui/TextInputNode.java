package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.bindable.BindableStringProperty;
import dev.prozilla.pine.common.property.bindable.SimpleBindableStringProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.standard.ui.text.TextRenderer;

public class TextInputNode extends Component {
	
	public int cursorPosition;
	public int selection;
	public Input.TextListener textListener;
	public Type type;
	public TextNode textNode;
	private BindableStringProperty textProperty;
	public String placeholder;
	public boolean isDragging;
	
	public Node placeholderNode;
	public TextNode placeholderTextNode;
	
	public static final String PLACEHOLDER_ELEMENT = "placeholder";
	
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
		this(new SimpleBindableStringProperty(""));
	}
	
	public TextInputNode(BindableStringProperty textProperty) {
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
	
	public BindableStringProperty getTextProperty() {
		return textProperty;
	}
	
	public void setTextProperty(BindableStringProperty textProperty) {
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
	
	public String getSelectedText() {
		if (!hasSelection()) {
			return null;
		}
		return textProperty.getValue().substring(getSelectionStart(), getSelectionEnd());
	}
	
	public void insert(String text) {
		deleteSelection();
		
		if (StringUtils.isEmpty(text)) {
			return;
		}
		
		boolean inserted = textProperty.buildValue((stringBuilder) -> {
			if (cursorPosition < 0 || cursorPosition > stringBuilder.length()) {
				return null;
			}
			String string = stringBuilder.insert(cursorPosition, text).toString();
			return type.isValid(string) ? string : null;
		});
		
		if (inserted) {
			cursorPosition += text.length();
		}
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
	
	public boolean deleteSelection() {
		if (!hasSelection()) {
			return false;
		}
		
		cursorPosition = getSelectionStart();
		textProperty.buildValue((stringBuilder) -> stringBuilder.delete(cursorPosition, cursorPosition + Math.abs(selection)));
		clearSelection();
		return true;
	}
	
	public void moveCursorToStart(boolean select) {
		moveCursorTo(0, select);
	}
	
	public void moveCursorToEnd(boolean select) {
		moveCursorTo(textProperty.getLength(), select);
	}
	
	public void moveCursorTo(int position, boolean select) {
		if (select) {
			selection += cursorPosition - position;
		} else {
			clearSelection();
		}
		cursorPosition = position;
		updateCursor();
	}
	
	public void moveCursor(int delta) {
		if (delta == 0) {
			return;
		}
		
		if (!hasSelection()) {
			cursorPosition += delta;
		} else {
			cursorPosition = delta < 0 ? getSelectionStart() : getSelectionEnd();
		}
		
		clearSelection();
		updateCursor();
	}
	
	public void clearSelection() {
		selection = 0;
	}
	
	public void dragSelection(int delta) {
		if (delta == 0) {
			return;
		}
		
		boolean isCursorAtEdge = delta < 0 ? isCursorAtStart() : isCursorAtEnd();
		if (isCursorAtEdge) {
			return;
		}
		
		cursorPosition += delta;
		selection -= delta;
		updateCursor();
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
	
	public void selectAll() {
		cursorPosition = textProperty.getLength();
		selection = -cursorPosition;
		updateCursor();
	}
	
	public void updateCursor() {
		cursorPosition = MathUtils.clamp(cursorPosition, 0, textProperty.getLength());
		selection = MathUtils.clamp(cursorPosition + selection, 0, textProperty.getLength()) - cursorPosition;
	}
	
	public int getCursorPosition(float cursor) {
		int min = 0;
		int max = textProperty.getLength();
		
		if (cursor <= 0) {
			return 0;
		} else if (cursor >= TextRenderer.getTextWidth(getRenderer(), textNode, max)) {
			return max;
		}
		
		while (min < max) {
			int middle = (min + max) / 2;
			float width = TextRenderer.getTextWidth(getRenderer(), textNode, middle);
			
			if (width < cursor - MathUtils.EPSILON) {
				min = middle + 1;
			} else {
				max = middle;
			}
		}
		
		if (min > 0) {
			float guessedWidth = TextRenderer.getTextWidth(getRenderer(), textNode, min);
			float alternativeWidth = TextRenderer.getTextWidth(getRenderer(), textNode, min - 1);
			if (Math.abs(cursor - alternativeWidth) < Math.abs(guessedWidth - cursor)) {
				return min - 1;
			}
		}
		
		return min;
	}
	
}
