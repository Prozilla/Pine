package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for text elements in the UI.
 */
public class TextPrefab extends CanvasElementPrefab {
	
	protected String text;
	protected Font font;
	
	public TextPrefab() {
		this(null);
	}
	
	public TextPrefab(String text) {
		this(text, Color.white());
	}
	
	public TextPrefab(String text, Color color) {
		this.text = text;
		setColor(color);
		setName("Text");
		addClass("text");
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setFont(String fontPath) {
		setFont(ResourcePool.loadFont(fontPath));
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextRenderer textRenderer = entity.addComponent(new TextRenderer(text));
		
		if (font != null) {
			textRenderer.setFont(font);
		} else {
			Font defaultFont = entity.getApplication().getDefaultFont();
			if (defaultFont != null) {
				textRenderer.setFont(defaultFont);
			}
		}
	}
}
