package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.*;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Set;

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
	private StyledCursorProperty cursorProperty;
	private StyledDimensionProperty borderWidthProperty;
	private StyledBorderStyleProperty borderStyleProperty;
	
	public NodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public NodeStyle(AnimationData animationData, Node node, Set<StyleSheet> styleSheets) {
		super(animationData, node, styleSheets);
	}
	
	@Override
	public boolean applyStyleSheet(StyleSheet styleSheet) {
		if (!super.applyStyleSheet(styleSheet)) {
			return false;
		}
		
		setColorProperty(styleSheet.createColorProperty(node));
		setBackgroundColorProperty(styleSheet.createBackgroundColorProperty(node));
		setSizeProperty(styleSheet.createSizeProperty(node));
		setPaddingProperty(styleSheet.createPaddingProperty(node));
		setMarginProperty(styleSheet.createMarginProperty(node));
		setAnchorProperty(styleSheet.createAnchorProperty(node));
		setCursorProperty(styleSheet.createCursorProperty(node));
		setBorderWidthProperty(styleSheet.createBorderWidthProperty(node));
		setBorderStyleProperty(styleSheet.createBorderStyleProperty(node));
		
		return true;
	}
	
	public StyledColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyledColorProperty colorProperty) {
		this.colorProperty = changeProperty(this.colorProperty, colorProperty);
	}
	
	public StyledColorProperty getBackgroundColorProperty() {
		return backgroundColorProperty;
	}
	
	public void setBackgroundColorProperty(StyledColorProperty backgroundColorProperty) {
		this.backgroundColorProperty = changeProperty(this.backgroundColorProperty, backgroundColorProperty);
	}
	
	public StyledDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(StyledDualDimensionProperty sizeProperty) {
		this.sizeProperty = changeProperty(this.sizeProperty, sizeProperty);
	}
	
	public StyledDualDimensionProperty getPaddingProperty() {
		return paddingProperty;
	}
	
	public void setPaddingProperty(StyledDualDimensionProperty paddingProperty) {
		this.paddingProperty = changeProperty(this.paddingProperty, paddingProperty);
	}
	
	public StyledDualDimensionProperty getMarginProperty() {
		return marginProperty;
	}
	
	public void setMarginProperty(StyledDualDimensionProperty marginProperty) {
		this.marginProperty = changeProperty(this.marginProperty, marginProperty);
	}
	
	public StyledGridAlignmentProperty getAnchorProperty() {
		return anchorProperty;
	}
	
	public void setAnchorProperty(StyledGridAlignmentProperty anchorProperty) {
		this.anchorProperty = changeProperty(this.anchorProperty, anchorProperty);
	}
	
	public StyledCursorProperty getCursorProperty() {
		return cursorProperty;
	}
	
	public void setCursorProperty(StyledCursorProperty cursorProperty) {
		this.cursorProperty = changeProperty(this.cursorProperty, cursorProperty);
	}
	
	public StyledDimensionProperty getBorderWidthProperty() {
		return borderWidthProperty;
	}
	
	public void setBorderWidthProperty(StyledDimensionProperty borderWidthProperty) {
		this.borderWidthProperty = changeProperty(this.borderWidthProperty, borderWidthProperty);
	}
	
	public StyledBorderStyleProperty getBorderStyleProperty() {
		return borderStyleProperty;
	}
	
	public void setBorderStyleProperty(StyledBorderStyleProperty borderStyleProperty) {
		this.borderStyleProperty = changeProperty(this.borderStyleProperty, borderStyleProperty);
	}
	
}
