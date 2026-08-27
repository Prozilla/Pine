package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.RangeInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.prefab.ui.NodePrefab;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

public class RangeInputInitializer extends InputSystem {
	
	public RangeInputInitializer() {
		super(RangeInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		RangeInputNode rangeInputNode = chunk.getComponent(RangeInputNode.class);
		Node node = chunk.getComponent(Node.class);
		
		rangeInputNode.progressNode = node.addPseudoElement( RangeInputNode.PROGRESS_ELEMENT,new NodePrefab());
		rangeInputNode.thumbNode = node.addPseudoElement( RangeInputNode.THUMB_ELEMENT,new NodePrefab());
		rangeInputNode.trackNode = node.addPseudoElement( RangeInputNode.TRACK_ELEMENT,new NodePrefab());
	}
	
}
