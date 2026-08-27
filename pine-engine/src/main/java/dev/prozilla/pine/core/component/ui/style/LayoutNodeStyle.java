package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.*;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Set;

public class LayoutNodeStyle extends NodeStyleBase {
	
	protected StyledDimensionProperty gapProperty;
	protected StyledDirectionProperty directionProperty;
	protected StyledEdgeAlignmentProperty alignmentProperty;
	protected StyledDistributionProperty distributionProperty;
	
	public LayoutNodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public LayoutNodeStyle(AnimationData animationData, Node node, Set<StyleSheet> styleSheets) {
		super(animationData, node, styleSheets);
	}
	
	@Override
	public boolean applyStyleSheet(StyleSheet styleSheet) {
		if (!super.applyStyleSheet(styleSheet)) {
			return false;
		}
		
		setGapProperty(styleSheet.createGapProperty(node));
		setDirectionProperty(styleSheet.createDirectionProperty(node));
		setAlignmentProperty(styleSheet.createAlignmentProperty(node));
		setDistributionProperty(styleSheet.createDistributionProperty(node));
		
		return true;
	}
	
	public StyledDimensionProperty getGapProperty() {
		return gapProperty;
	}
	
	public void setGapProperty(StyledDimensionProperty gapProperty) {
		this.gapProperty = changeProperty(this.gapProperty, gapProperty);
	}
	
	public StyledDirectionProperty getDirectionProperty() {
		return directionProperty;
	}
	
	public void setDirectionProperty(StyledDirectionProperty directionProperty) {
		this.directionProperty = changeProperty(this.directionProperty, directionProperty);
	}
	
	public StyledEdgeAlignmentProperty getAlignmentProperty() {
		return alignmentProperty;
	}
	
	public void setAlignmentProperty(StyledEdgeAlignmentProperty alignmentProperty) {
		this.alignmentProperty = changeProperty(this.alignmentProperty, alignmentProperty);
	}
	
	public StyledDistributionProperty getDistributionProperty() {
		return distributionProperty;
	}
	
	public void setDistributionProperty(StyledDistributionProperty distributionProperty) {
		this.distributionProperty = changeProperty(this.distributionProperty, distributionProperty);
	}
	
}
