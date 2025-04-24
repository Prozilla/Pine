package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveStringProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.DynamicText;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for text elements in the UI.
 */
@Components({ TextNode.class, Node.class, Transform.class })
public class TextPrefab extends NodePrefab {
	
	protected String text;
	protected Font font;
	
	protected AdaptiveStringProperty textProperty;
	
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
	
	public void setText(VariableProperty<String> textProperty) {
		this.textProperty = AdaptiveStringProperty.adapt(textProperty);
	}
	
	public void setText(String text) {
		this.text = text;
		textProperty = null;
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
		
		TextNode textNode = entity.addComponent(new TextNode(text));
		
		if (font != null) {
			textNode.setFont(font);
		} else {
			Font defaultFont = entity.getApplication().getDefaultFont();
			if (defaultFont != null) {
				textNode.setFont(defaultFont);
			}
		}
		
		if (textProperty != null) {
			entity.addComponent(new DynamicText(textProperty));
		}
	}
}
