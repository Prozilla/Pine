package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledDimensionProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Set;

public class TextNodeStyle extends NodeStyleBase {
	
	protected StyledDimensionProperty fontSizeProperty;
	
	public TextNodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public TextNodeStyle(AnimationData animationData, Node node, Set<StyleSheet> styleSheets) {
		super(animationData, node, styleSheets);
	}
	
	@Override
	protected void createProperties() {
		StyleSheet styleSheet = StyleSheet.mergeAll(getStyleSheets());
		
		setFontSizeProperty(styleSheet.createFontSizeProperty(node));
	}
	
	public StyledDimensionProperty getFontSizeProperty() {
		return fontSizeProperty;
	}
	
	public void setFontSizeProperty(StyledDimensionProperty fontSizeProperty) {
		this.fontSizeProperty = changeProperty(this.fontSizeProperty, fontSizeProperty);
	}
	
}
