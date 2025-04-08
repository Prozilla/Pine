package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

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
		setFont(ResourcePool.loadFont(fontPath, size));
	}
	
	public void setFont(String fontPath) {
		setFont(ResourcePool.loadFont(fontPath));
	}
	
	/**
	 * Setter for the font of this text.
	 */
	public void setFont(Font font) {
		this.font = font;
		calculateSize();
	}
	
	public void setText(String text) {
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
