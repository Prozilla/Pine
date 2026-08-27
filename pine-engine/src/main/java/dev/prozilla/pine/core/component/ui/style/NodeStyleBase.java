package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledProperty;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.HashSet;
import java.util.Set;

public abstract class NodeStyleBase extends Component {
	
	// TODO: Order of stylesheets overrides selector specificity
	private final Set<StyleSheet> styleSheets;
	protected final Node node;
	private final AnimationData animationData;
	
	public NodeStyleBase(AnimationData animationData, Node node, Set<StyleSheet> styleSheets) {
		this.animationData = animationData;
		this.node = node;

		this.styleSheets = new HashSet<>();
		
		if (styleSheets != null) {
			for (StyleSheet styleSheet : styleSheets) {
				applyStyleSheet(styleSheet);
			}
		}
	}
	
	public Set<StyleSheet> getStyleSheets() {
		return styleSheets;
	}
	
	public boolean applyStyleSheet(StyleSheet styleSheet) {
		boolean added = styleSheets.add(styleSheet);
		if (added) {
			for (Node pseudoElement : node.pseudoElements.values()) {
				pseudoElement.addStyleSheet(styleSheet);
			}
			for (Node childNode : node.children) {
				childNode.addStyleSheet(styleSheet);
			}
		}
		return added;
	}
	
	protected <T, P extends Property<T>, A extends AdaptiveProperty<T, P>, R extends TransitionedProperty<T>, S extends StyledProperty<T, P, A, R>> S changeProperty(S oldProperty, S newProperty) {
		animationData.replaceProperty(oldProperty, newProperty);
		if (newProperty != null) {
			newProperty.invalidate();
		}
		return newProperty;
	}
	
}
