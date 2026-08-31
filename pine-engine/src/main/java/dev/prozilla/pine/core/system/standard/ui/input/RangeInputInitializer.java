package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.RangeInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class RangeInputInitializer extends InitSystem {
	
	public RangeInputInitializer() {
		super(RangeInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		RangeInputNode rangeInputNode = chunk.getComponent(RangeInputNode.class);
		Node node = chunk.getComponent(Node.class);
		
		node.setAttribute(Node.TYPE_ATTRIBUTE, "range");
		
		// TODO: Reduce code duplication
		rangeInputNode.trackNode = node.addPseudoElement(RangeInputNode.TRACK_ELEMENT);
		rangeInputNode.trackNode.controlledRender = false;
		rangeInputNode.trackNode.overwriteSize(new DualDimension(new Dimension.Dynamic(() -> node.currentInnerSize.x), new Dimension.Dynamic(() -> node.currentInnerSize.y)));
		rangeInputNode.trackNode.overwriteMargin(new DualDimension(new Dimension.Dynamic(() -> node.currentPosition.x), new Dimension.Dynamic(() -> node.currentPosition.y)));
		
		rangeInputNode.progressNode = node.addPseudoElement(RangeInputNode.PROGRESS_ELEMENT);
		rangeInputNode.progressNode.controlledRender = false;
		rangeInputNode.progressNode.overwriteSize(new DualDimension(new Dimension.Dynamic(() -> node.currentInnerSize.x * rangeInputNode.getProgress()), new Dimension.Dynamic(() -> node.currentInnerSize.y)));
		rangeInputNode.progressNode.overwriteMargin(new DualDimension(new Dimension.Dynamic(() -> node.currentPosition.x), new Dimension.Dynamic(() -> node.currentPosition.y)));
		
		rangeInputNode.thumbNode = node.addPseudoElement(RangeInputNode.THUMB_ELEMENT);
		rangeInputNode.thumbNode.controlledRender = false;
		rangeInputNode.thumbNode.overwriteSize(new DualDimension(new Dimension.Dynamic(() -> node.currentInnerSize.y)));
		rangeInputNode.thumbNode.overwriteMargin(new DualDimension(new Dimension.Dynamic(() -> node.currentPosition.x + node.currentInnerSize.x * rangeInputNode.getProgress() - node.currentInnerSize.y / 2f), new Dimension.Dynamic(() -> node.currentPosition.y)));
	}
	
}
