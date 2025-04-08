package dev.prozilla.pine.core.system.standard.ui.tooltip;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TooltipNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class TooltipInitializer extends InitSystem {
	
	public TooltipInitializer() {
		super(TooltipNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		TooltipNode tooltipNode = chunk.getComponent(TooltipNode.class);
		Node node = chunk.getComponent(Node.class);
		
		node.position = new DualDimension(
			Dimension.add(tooltipNode.cursorX, tooltipNode.baseX, tooltipNode.offset.x),
			Dimension.add(tooltipNode.cursorY, tooltipNode.baseY, tooltipNode.offset.y)
		);
	}
}
