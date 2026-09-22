package dev.prozilla.pine.core.component.ui.style;

import dev.prozilla.pine.common.property.style.*;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.Set;

public class LayoutNodeStyle extends NodeStyleBase {
	
	protected StyledDimensionProperty gapProperty;
	protected StyledDirectionProperty directionProperty;
	protected StyledAlignmentProperty alignmentProperty;
	protected StyledDistributionProperty distributionProperty;
	
	public LayoutNodeStyle(AnimationData animationData, Node node) {
		this(animationData, node, null);
	}
	
	public LayoutNodeStyle(AnimationData animationData, Node node, Set<StyleSheet> styleSheets) {
		super(animationData, node, styleSheets);
	}
	
	@Override
	protected void createProperties() {
		StyleSheet styleSheet = StyleSheet.mergeAll(getStyleSheets());
		
		setGapProperty(styleSheet.createGapProperty(node));
		setDirectionProperty(styleSheet.createDirectionProperty(node));
		setAlignmentProperty(styleSheet.createAlignmentProperty(node));
		setDistributionProperty(styleSheet.createDistributionProperty(node));
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
	
	public StyledAlignmentProperty getAlignmentProperty() {
		return alignmentProperty;
	}
	
	public void setAlignmentProperty(StyledAlignmentProperty alignmentProperty) {
		this.alignmentProperty = changeProperty(this.alignmentProperty, alignmentProperty);
	}
	
	public StyledDistributionProperty getDistributionProperty() {
		return distributionProperty;
	}
	
	public void setDistributionProperty(StyledDistributionProperty distributionProperty) {
		this.distributionProperty = changeProperty(this.distributionProperty, distributionProperty);
	}
	
}
