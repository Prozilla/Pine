package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for text elements in the UI.
 */
public class TextPrefab extends CanvasElementPrefab {
	
	protected String text;
	protected Color color;
	protected Font font;
	
	public TextPrefab() {
		this(null);
	}
	
	public TextPrefab(String text) {
		this.text = text;
		setName("Text");
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextRenderer textRenderer = entity.addComponent(new TextRenderer(text));
		
		if (color != null) {
			textRenderer.color = color;
		}
		if (font != null) {
			textRenderer.setFont(font);
		}
	}
}
