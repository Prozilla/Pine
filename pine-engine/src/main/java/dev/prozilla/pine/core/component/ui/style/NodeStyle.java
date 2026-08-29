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
	private StyledLineStyleProperty borderStyleProperty;
	private StyledColorProperty borderColorProperty;
	private StyledDimensionProperty outlineWidthProperty;
	private StyledLineStyleProperty outlineStyleProperty;
	private StyledColorProperty outlineColorProperty;
	private StyledDimensionProperty outlineOffsetProperty;
	
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
		setBorderColorProperty(styleSheet.createBorderColorProperty(node));
		setOutlineWidthProperty(styleSheet.createOutlineWidthProperty(node));
		setOutlineStyleProperty(styleSheet.createOutlineStyleProperty(node));
		setOutlineColorProperty(styleSheet.createOutlineColorProperty(node));
		setOutlineOffsetProperty(styleSheet.createOutlineOffsetProperty(node));
		
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
	
	public StyledLineStyleProperty getBorderStyleProperty() {
		return borderStyleProperty;
	}
	
	public void setBorderStyleProperty(StyledLineStyleProperty borderStyleProperty) {
		this.borderStyleProperty = changeProperty(this.borderStyleProperty, borderStyleProperty);
	}
	
	public StyledColorProperty getBorderColorProperty() {
		return borderColorProperty;
	}
	
	public void setBorderColorProperty(StyledColorProperty borderColorProperty) {
		this.borderColorProperty = changeProperty(this.borderColorProperty, borderColorProperty);
	}
	
	public StyledDimensionProperty getOutlineWidthProperty() {
		return outlineWidthProperty;
	}
	
	public void setOutlineWidthProperty(StyledDimensionProperty outlineWidthProperty) {
		this.outlineWidthProperty = changeProperty(this.outlineWidthProperty, outlineWidthProperty);
	}
	
	public StyledLineStyleProperty getOutlineStyleProperty() {
		return outlineStyleProperty;
	}
	
	public void setOutlineStyleProperty(StyledLineStyleProperty outlineStyleProperty) {
		this.outlineStyleProperty = changeProperty(this.outlineStyleProperty, outlineStyleProperty);
	}
	
	public StyledColorProperty getOutlineColorProperty() {
		return outlineColorProperty;
	}
	
	public void setOutlineColorProperty(StyledColorProperty outlineColorProperty) {
		this.outlineColorProperty = changeProperty(this.outlineColorProperty, outlineColorProperty);
	}
	
	public StyledDimensionProperty getOutlineOffsetProperty() {
		return outlineOffsetProperty;
	}
	
	public void setOutlineOffsetProperty(StyledDimensionProperty outlineOffsetProperty) {
		this.outlineOffsetProperty = changeProperty(this.outlineOffsetProperty, outlineOffsetProperty);
	}
	
}
