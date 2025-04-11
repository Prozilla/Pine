package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledIntProperty;

public class LayoutNodeStyle extends NodeStyleBase {
	
	protected StyledIntProperty gapProperty;
	
	public LayoutNodeStyle(Node node) {
		this(node, null);
	}
	
	public LayoutNodeStyle(Node node, StyleSheet styleSheet) {
		super(node, styleSheet);
	}
	
	@Override
	public void applyStyleSheet(StyleSheet styleSheet) {
		setGapProperty(styleSheet.createGapProperty(node));
	}
	
	public StyledIntProperty getGapProperty() {
		return gapProperty;
	}
	
	public void setGapProperty(StyledIntProperty gapProperty) {
		changeProperty(this.gapProperty, gapProperty);
		this.gapProperty = gapProperty;
	}
	
}
