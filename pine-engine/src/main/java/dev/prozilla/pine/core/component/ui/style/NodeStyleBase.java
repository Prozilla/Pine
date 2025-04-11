package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledProperty;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

public abstract class NodeStyleBase extends Component {
	
	protected final Node node;
	private final AnimationData animationData;
	
	public NodeStyleBase(AnimationData animationData, Node node, StyleSheet styleSheet) {
		this.animationData = animationData;
		this.node = node;
		
		if (styleSheet != null) {
			applyStyleSheet(styleSheet);
		}
	}
	
	abstract void applyStyleSheet(StyleSheet styleSheet);
	
	protected void changeProperty(StyledProperty<?> oldProperty, StyledProperty<?> newProperty) {
		animationData.replaceProperty(oldProperty, newProperty);
		if (newProperty != null) {
			newProperty.invalidate();
		}
	}
	
}
