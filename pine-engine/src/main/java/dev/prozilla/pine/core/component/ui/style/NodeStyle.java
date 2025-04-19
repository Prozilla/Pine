package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledColorProperty;
import dev.prozilla.pine.common.property.style.StyledDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyledGridAlignmentProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

/**
 * A component that applies styles to nodes.
 */
public class NodeStyle extends NodeStyleBase {
	
	private StyledColorProperty colorProperty;
	private StyledColorProperty backgroundColorProperty;
	private StyledDualDimensionProperty sizeProperty;
	private StyledDualDimensionProperty paddingProperty;
	private StyledDualDimensionProperty marginProperty;
	private StyledGridAlignmentProperty anchorProperty;
	
	public NodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public NodeStyle(AnimationData animationData, Node node, StyleSheet styleSheet) {
		super(animationData, node, styleSheet);
	}
	
	@Override
	public void applyStyleSheet(StyleSheet styleSheet) {
		setColorProperty(styleSheet.createColorProperty(node));
		setBackgroundColorProperty(styleSheet.createBackgroundColorProperty(node));
		setSizeProperty(styleSheet.createSizeProperty(node));
		setPaddingProperty(styleSheet.createPaddingProperty(node));
		setMarginProperty(styleSheet.createMarginProperty(node));
		setAnchorProperty(styleSheet.createAnchorProperty(node));
	}
	
	public StyledColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyledColorProperty colorProperty) {
		changeProperty(this.colorProperty, colorProperty);
		this.colorProperty = colorProperty;
	}
	
	public StyledColorProperty getBackgroundColorProperty() {
		return backgroundColorProperty;
	}
	
	public void setBackgroundColorProperty(StyledColorProperty backgroundColorProperty) {
		changeProperty(this.backgroundColorProperty, backgroundColorProperty);
		this.backgroundColorProperty = backgroundColorProperty;
	}
	
	public StyledDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(StyledDualDimensionProperty sizeProperty) {
		changeProperty(this.sizeProperty, sizeProperty);
		this.sizeProperty = sizeProperty;
	}
	
	public StyledDualDimensionProperty getPaddingProperty() {
		return paddingProperty;
	}
	
	public void setPaddingProperty(StyledDualDimensionProperty paddingProperty) {
		changeProperty(this.paddingProperty, paddingProperty);
		this.paddingProperty = paddingProperty;
	}
	
	public StyledDualDimensionProperty getMarginProperty() {
		return marginProperty;
	}
	
	public void setMarginProperty(StyledDualDimensionProperty marginProperty) {
		changeProperty(this.marginProperty, marginProperty);
		this.marginProperty = marginProperty;
	}
	
	public StyledGridAlignmentProperty getAnchorProperty() {
		return anchorProperty;
	}
	
	public void setAnchorProperty(StyledGridAlignmentProperty anchorProperty) {
		changeProperty(this.anchorProperty, anchorProperty);
		this.anchorProperty = anchorProperty;
	}
}
