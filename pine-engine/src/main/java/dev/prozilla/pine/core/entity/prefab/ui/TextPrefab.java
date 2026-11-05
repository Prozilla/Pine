package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.TriState;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveStringProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.DynamicText;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.component.ui.style.TextNodeStyle;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for text elements in the UI.
 */
@Components({ TextNode.class, Node.class, Transform.class })
public class TextPrefab extends NodePrefab {
	
	protected String text;
	protected Font font;
	protected DimensionBase fontSize;
	
	protected AdaptiveStringProperty textProperty;
	
	public TextPrefab() {
		this((String)null);
	}
	
	public TextPrefab(StringProperty textProperty) {
		this(textProperty, Color.white());
	}
	
	public TextPrefab(StringProperty textProperty, Color color) {
		this(textProperty.getValue(), color);
		setText(textProperty);
	}
	
	public TextPrefab(String text) {
		this(text, Color.white());
	}
	
	public TextPrefab(String text, Color color) {
		this.text = text;
		
		setColor(color);
		setName("Text");
		addClass("text");
		setHTMLTag(Node.PARAGRAPH_TAG);
		
		fontSize = new Dimension(Font.DEFAULT_SIZE);
	}
	
	public void setText(StringProperty textProperty) {
		this.textProperty = AdaptiveStringProperty.adapt(textProperty);
	}
	
	public void setText(String text) {
		this.text = text;
		textProperty = null;
	}
	
	public void setFont(String fontPath) {
		setFont(AssetPools.fonts.load(fontPath));
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setFontSize(DimensionBase fontSize) {
		this.fontSize = fontSize;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextNode textNode = entity.addComponent(new TextNode(text));
		textNode.fontSize = fontSize;
		
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
		
		if (!styleSheets.isEmpty() || useDefaultStyleSheet == TriState.TRUE) {
			Node node = entity.getComponent(Node.class);
			AnimationData animationData = entity.getComponent(AnimationData.class);
			entity.addComponent(new TextNodeStyle(animationData, node, node.getStyleSheets()));
		}
	}
}
