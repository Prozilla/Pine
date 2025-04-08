package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledColorProperty;
import dev.prozilla.pine.common.property.style.StyledDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyledProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that applies styles to nodes.
 */
public class NodeStyle extends AnimationData {
	
	protected final Node node;
	
	protected StyledColorProperty colorProperty;
	protected StyledColorProperty backgroundColorProperty;
	protected StyledDualDimensionProperty sizeProperty;
	protected StyledDualDimensionProperty paddingProperty;
	protected StyledDualDimensionProperty positionProperty;
	
	public NodeStyle(Node node) {
		this(node, null);
	}
	
	public NodeStyle(Node node, StyleSheet styleSheet) {
		super(false);
		this.node = node;
		
		if (styleSheet != null) {
			applyStyleSheet(styleSheet);
		}
	}
	
	public void applyStyleSheet(StyleSheet styleSheet) {
		setColorProperty(styleSheet.createColorProperty(node));
		setBackgroundColorProperty(styleSheet.createBackgroundColorProperty(node));
		setSizeProperty(styleSheet.createSizeProperty(node));
		setPaddingProperty(styleSheet.createPaddingProperty(node));
		setPositionProperty(styleSheet.createPositionProperty(node));
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
	
	public StyledDualDimensionProperty getPositionProperty() {
		return positionProperty;
	}
	
	public void setPositionProperty(StyledDualDimensionProperty positionProperty) {
		changeProperty(this.positionProperty, positionProperty);
		this.positionProperty = positionProperty;
	}
	
	protected void changeProperty(StyledProperty<?> oldProperty, StyledProperty<?> newProperty) {
		super.changeProperty(oldProperty, newProperty);
		if (newProperty != null) {
			newProperty.invalidate();
		}
	}
}
