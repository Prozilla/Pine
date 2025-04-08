package dev.prozilla.pine.core.system.standard.ui.frame;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.FrameNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.FrameBufferObject;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class FrameRenderer extends RenderSystem {
	
	public FrameRenderer() {
		super(Node.class, FrameNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		Node node = chunk.getComponent(Node.class);
		FrameNode frame = chunk.getComponent(FrameNode.class);
		
		// Draw the background
		if (frame.backgroundColor != null) {
			renderer.drawRect(node.currentPosition.x, node.currentPosition.y, transform.getDepth(), frame.getWidth(), frame.getHeight());
		}
		
		// Draw the frame buffer
		FrameBufferObject fbo = frame.fbo;
		if (fbo != null) {
			renderer.drawTexture(fbo.getTexture(), node.currentPosition.x, node.currentPosition.y, transform.getDepth());
		}
	}
}
