package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class NodeInitializer extends InitSystem {
	
	public NodeInitializer() {
		super(Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Entity entity = chunk.getEntity();
		Node node = chunk.getComponent(Node.class);
		node.updateHierarchy();
		
		LayoutNode group = entity.getComponentInParent(LayoutNode.class, false);
		if (group != null) {
			group.getChildComponents();
		}
		
		// Make sure node is correct positioned if activated after the RectUpdater system
		// TO DO: optimize this so it only gets triggered if a node was activated after the RectUpdater system
		NodeUpdater.updateNode(node);
	}
}
