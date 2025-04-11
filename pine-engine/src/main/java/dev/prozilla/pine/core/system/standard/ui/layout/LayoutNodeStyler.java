package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.style.LayoutNodeStyle;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class LayoutNodeStyler extends UpdateSystem {
	
	public LayoutNodeStyler() {
		super( LayoutNode.class, LayoutNodeStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		LayoutNode layoutNode = chunk.getComponent(LayoutNode.class);
		LayoutNodeStyle layoutNodeStyle = chunk.getComponent(LayoutNodeStyle.class);
		
		if (layoutNodeStyle.getGapProperty() != null) {
			layoutNode.gap = layoutNodeStyle.getGapProperty().getValue();
		}
		if (layoutNodeStyle.getDirectionProperty() != null) {
			layoutNode.direction = layoutNodeStyle.getDirectionProperty().getValue();
		}
		if (layoutNodeStyle.getAlignmentProperty() != null) {
			layoutNode.alignment = layoutNodeStyle.getAlignmentProperty().getValue();
		}
		if (layoutNodeStyle.getDistributionProperty() != null) {
			layoutNode.distribution = layoutNodeStyle.getDistributionProperty().getValue();
		}
	}
	
}
