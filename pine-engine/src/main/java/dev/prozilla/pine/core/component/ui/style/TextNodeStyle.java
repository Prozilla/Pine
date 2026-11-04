package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.property.style.StyledDimensionProperty;
import dev.prozilla.pine.common.property.style.StyledStringProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Objects;
import java.util.Set;

public class TextNodeStyle extends NodeStyleBase {
	
	protected StyledDimensionProperty fontSizeProperty;
	protected StyledStringProperty fontFamilyProperty;
	
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
		setFontFamilyProperty(styleSheet.createFontFamilyProperty(node));
	}
	
	public StyledDimensionProperty getFontSizeProperty() {
		return fontSizeProperty;
	}
	
	public void setFontSizeProperty(StyledDimensionProperty fontSizeProperty) {
		this.fontSizeProperty = changeProperty(this.fontSizeProperty, fontSizeProperty);
	}
	
	public Font getFont() {
		String name = null;
		if (fontFamilyProperty != null) {
			name = fontFamilyProperty.getValue();
		}
		
		if (name != null && !Objects.equals(name, StyleSheet.DEFAULT_FONT_FAMILY)) {
			for (StyleSheet styleSheet : getStyleSheets()) {
				Font font = styleSheet.getFontFamily(name);
				if (font != null) {
					return font;
				}
			}
			
			String fontPath = switch (name.toLowerCase()) {
				case "sans-serif" -> Font.BODY_FONT_PATH;
				case "monospace" -> Font.MONO_FONT_PATH;
				default -> null;
			};
			if (fontPath != null) {
				return AssetPools.fonts.load(fontPath);
			}
		}
		
		return null;
	}
	
	public StyledStringProperty getFontFamilyProperty() {
		return fontFamilyProperty;
	}
	
	public void setFontFamilyProperty(StyledStringProperty fontFamilyProperty) {
		this.fontFamilyProperty = changeProperty(this.fontFamilyProperty, fontFamilyProperty);
	}
	
}
