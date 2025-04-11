package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledProperty;
import dev.prozilla.pine.core.component.AnimationData;

public abstract class NodeStyleBase extends AnimationData {
	
	protected final Node node;
	
	public NodeStyleBase(Node node, StyleSheet styleSheet) {
		super(false);
		this.node = node;
		
		if (styleSheet != null) {
			applyStyleSheet(styleSheet);
		}
	}
	
	abstract void applyStyleSheet(StyleSheet styleSheet);
	
	protected void changeProperty(StyledProperty<?> oldProperty, StyledProperty<?> newProperty) {
		super.changeProperty(oldProperty, newProperty);
		if (newProperty != null) {
			newProperty.invalidate();
		}
	}
	
}
