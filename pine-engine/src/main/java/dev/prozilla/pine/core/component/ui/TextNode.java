package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.function.Function;

/**
 * A component for rendering text in the UI.
 */
public class TextNode extends Component {
	
	public String text;
	public Font font;
	
	/** Result of size calculation */
	public Vector2i size;
	
	public TextNode() {
		this(null);
	}
	
	public TextNode(String text) {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
		}
		
		size = new Vector2i();
	}
	
	@Override
	public String getName() {
		return "TextRenderer";
	}
	
	public void setFont(String fontPath, int size) {
		setFont(AssetPools.fonts.load(fontPath, size));
	}
	
	public void setFont(String fontPath) {
		setFont(AssetPools.fonts.load(fontPath));
	}
	
	/**
	 * Setter for the font of this text.
	 */
	public void setFont(Font font) {
		this.font = font;
		calculateSize();
	}
	
	public int getFontSize() {
		if (font != null) {
			return font.getSize();
		}
		
		return Font.DEFAULT_SIZE;
	}
	
	public boolean changeText(Function<StringBuilder, String> textModifier) {
		String newText = textModifier.apply(new StringBuilder(text));
		if (newText == null || text.equals(newText)) {
			return false;
		}
		setText(newText);
		return true;
	}
	
	public void setText(String text) {
		Checks.isNotNull(text, "text");
		
		if (this.text.equals(text)) {
			return;
		}
		
		this.text = text;
		calculateSize();
	}
	
	/**
	 * Calculates the size of this text.
	 */
	public void calculateSize() {
		Renderer renderer = getRenderer();
		
		if (font == null) {
			size = renderer.getTextSize(text);
		} else {
			size = renderer.getTextSize(font, text);
		}
	}
}
