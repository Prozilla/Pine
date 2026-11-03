package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

import java.util.Objects;
import java.util.function.Function;

/**
 * A component for rendering text in the UI.
 */
public class TextNode extends Component {
	
	public String text;
	public Font font;
	public DimensionBase fontSize;
	
	/** Result of size calculation */
	public Vector2f size;
	public Vector2f offset;
	
	public Node node;
	
	public TextNode() {
		this(null);
	}
	
	public TextNode(String text) {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
		}
		
		size = new Vector2f();
		fontSize = new Dimension(Font.DEFAULT_SIZE);
	}
	
	@Override
	public String getName() {
		return "TextRenderer";
	}
	
	public void setFont(String fontPath) {
		setFont(AssetPools.fonts.load(fontPath));
	}
	
	/**
	 * Setter for the font of this text.
	 */
	public void setFont(Font font) {
		if (Objects.equals(this.font, font)) {
			return;
		}
		this.font = font;
		calculateSize();
	}
	
	public void setFontSize(DimensionBase fontSize) {
		if (Objects.equals(this.fontSize, fontSize)) {
			return;
		}
		this.fontSize = fontSize;
		calculateSize();
	}
	
	public boolean changeText(Function<StringBuilder, String> textModifier) {
		String newText = textModifier.apply(new StringBuilder(text));
		if (newText == null || text.equals(newText)) {
			return false;
		}
		setText(newText);
		return true;
	}
	
	public boolean clearText() {
		return setText("");
	}
	
	public boolean setText(String text) {
		Checks.isNotNull(text, "text");
		
		if (this.text.equals(text)) {
			return false;
		}
		
		this.text = text;
		calculateSize();
		return true;
	}
	
	/**
	 * Calculates the size of this text.
	 */
	public void calculateSize() {
		Font font = this.font;
		if (font == null) {
			font = getRenderer().getDefaultFont();
		}
		size = font.getSize(text, getFontSize());
	}
	
	public float getFontSize() {
		return fontSize != null ? fontSize.compute(node, false) : 0;
	}
	
	public float getOffsetX() {
		return offset != null ? offset.x : 0;
	}
	
	public float getOffsetY() {
		return offset != null ? offset.y : 0;
	}
	
}
