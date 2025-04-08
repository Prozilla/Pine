package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders nodes to the screen.
 */
public class NodeRenderer extends RenderSystem {
	
	public NodeRenderer() {
		super(Node.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		Node node = chunk.getComponent(Node.class);
		
		if (!node.readyToRender) {
			return;
		}
		
		if (node.currentSize.x != 0 && node.currentSize.y != 0 && node.backgroundColor != null) {
			renderer.drawRect(node.currentPosition.x, node.currentPosition.y, transform.getDepth(), node.currentSize.x, node.currentSize.y, node.backgroundColor);
		}
	}
}
