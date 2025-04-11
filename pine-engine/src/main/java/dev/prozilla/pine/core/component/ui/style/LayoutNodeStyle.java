package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledIntProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

public class LayoutNodeStyle extends NodeStyleBase {
	
	protected StyledIntProperty gapProperty;
	
	public LayoutNodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public LayoutNodeStyle(AnimationData animationData, Node node, StyleSheet styleSheet) {
		super(animationData, node, styleSheet);
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
