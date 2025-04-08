package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TooltipNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

/**
 * Updates the <code>passThrough</code> properties of layout nodes based on their background color.
 */
public class LayoutNodeInputHandler extends InputSystem {
	
	public LayoutNodeInputHandler() {
		super(LayoutNode.class, Node.class);
		setExcludedComponentTypes(TooltipNode.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Input input, float deltaTime) {
		Node node = chunk.getComponent(Node.class);
		
		node.passThrough = (node.backgroundColor == null);
	}
}
