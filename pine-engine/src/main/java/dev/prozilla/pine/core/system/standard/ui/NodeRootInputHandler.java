package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class NodeRootInputHandler extends InputSystem {
	
	public NodeRootInputHandler() {
		super(NodeRoot.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		NodeRoot nodeRoot = chunk.getComponent(NodeRoot.class);
		
		if (nodeRoot.tooltip != null) {
			nodeRoot.tooltip.setActive(nodeRoot.tooltipActivator.cursorHit);
		}
		
		if (input.getKeyDown(Key.TAB)) {
			if (input.getKey(Key.L_SHIFT)) {
				nodeRoot.focusPreviousNode();
			} else {
				nodeRoot.focusNextNode();
			}
		}
	}
}
