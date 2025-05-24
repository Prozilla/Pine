package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.EntityEventType;
import dev.prozilla.pine.core.system.init.InitSystem;

/**
 * Initializes layout nodes by fetching their children.
 */
public class LayoutNodeInitializer extends InitSystem {
	
	public LayoutNodeInitializer() {
		super(LayoutNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		LayoutNode layoutNode = chunk.getComponent(LayoutNode.class);
		layoutNode.getChildComponents();
		
		// Fetch child nodes whenever children are added/removed
		chunk.getEntity().addListener(EntityEventType.CHILDREN_UPDATE, (entity) -> layoutNode.getChildComponents());
	}
}
