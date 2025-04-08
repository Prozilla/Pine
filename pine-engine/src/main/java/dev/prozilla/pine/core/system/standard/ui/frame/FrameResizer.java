package dev.prozilla.pine.core.system.standard.ui.frame;

import dev.prozilla.pine.core.component.ui.FrameNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class FrameResizer extends UpdateSystem {
	
	public FrameResizer() {
		super(Node.class, FrameNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Node node = chunk.getComponent(Node.class);
		FrameNode frame = chunk.getComponent(FrameNode.class);
		
		node.currentSize.x = frame.getWidth();
		node.currentSize.y = frame.getHeight();
	}
}
