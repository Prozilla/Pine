package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.*;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

public class LayoutNodeStyle extends NodeStyleBase {
	
	protected StyledDimensionProperty gapProperty;
	protected StyledDirectionProperty directionProperty;
	protected StyledEdgeAlignmentProperty alignmentProperty;
	protected StyledDistributionProperty distributionProperty;
	
	public LayoutNodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public LayoutNodeStyle(AnimationData animationData, Node node, StyleSheet styleSheet) {
		super(animationData, node, styleSheet);
	}
	
	@Override
	public void applyStyleSheet(StyleSheet styleSheet) {
		setGapProperty(styleSheet.createGapProperty(node));
		setDirectionProperty(styleSheet.createDirectionProperty(node));
		setAlignmentProperty(styleSheet.createAlignmentProperty(node));
		setDistributionProperty(styleSheet.createDistributionProperty(node));
	}
	
	public StyledDimensionProperty getGapProperty() {
		return gapProperty;
	}
	
	public void setGapProperty(StyledDimensionProperty gapProperty) {
		changeProperty(this.gapProperty, gapProperty);
		this.gapProperty = gapProperty;
	}
	
	public StyledDirectionProperty getDirectionProperty() {
		return directionProperty;
	}
	
	public void setDirectionProperty(StyledDirectionProperty directionProperty) {
		changeProperty(this.directionProperty, directionProperty);
		this.directionProperty = directionProperty;
	}
	
	public StyledEdgeAlignmentProperty getAlignmentProperty() {
		return alignmentProperty;
	}
	
	public void setAlignmentProperty(StyledEdgeAlignmentProperty alignmentProperty) {
		changeProperty(this.alignmentProperty, alignmentProperty);
		this.alignmentProperty = alignmentProperty;
	}
	
	public StyledDistributionProperty getDistributionProperty() {
		return distributionProperty;
	}
	
	public void setDistributionProperty(StyledDistributionProperty distributionProperty) {
		changeProperty(this.distributionProperty, distributionProperty);
		this.distributionProperty = distributionProperty;
	}
	
}
