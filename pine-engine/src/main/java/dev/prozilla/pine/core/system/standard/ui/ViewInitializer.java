package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.ViewNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class ViewInitializer extends InitSystem {
	
	public ViewInitializer() {
		super(Node.class, ViewNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Node node = chunk.getComponent(Node.class);
		ViewNode viewNode = chunk.getComponent(ViewNode.class);
		
		if (viewNode.controller != null) {
			viewNode.controller.initialize(node);
		}
	}
}
