package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.common.system.resource.Color;

/**
 * Represents a text element inside the canvas.
 * Wrapper object for the {@link TextRenderer} component.
 */
public class Text extends CanvasElement {
	
	protected final TextRenderer textRenderer;
	
	public Text(World world) {
		this(world, null);
	}
	
	public Text(World world, String text) {
		super(world);
		
		textRenderer = new TextRenderer(text);
		addComponent(textRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Text");
	}
	
	@Override
	public Text setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (Text)super.setPosition(anchor, x, y);
	}
	
	@Override
	public Text setAnchor(RectTransform.Anchor anchor) {
		return (Text)super.setAnchor(anchor);
	}
	
	@Override
	public Text setOffset(int x, int y) {
		return (Text)super.setOffset(x, y);
	}
	
	public String getText() {
		return textRenderer.text;
	}
	
	public Text setText(String text) {
		if (text.equals(textRenderer.text)) {
			return this;
		}
		
		textRenderer.text = text;
		textRenderer.calculateSize();
		return this;
	}
	
	public Text setFont(String path) {
		setFont(ResourcePool.loadFont(path));
		return this;
	}
	
	public Text setFont(Font font) {
		textRenderer.setFont(font);
		return this;
	}
	
	public Text setColor(Color color) {
		textRenderer.color = color;
		return this;
	}
}
