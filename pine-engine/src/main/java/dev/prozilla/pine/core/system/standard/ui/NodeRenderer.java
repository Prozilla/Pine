package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderPass;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders nodes to the screen.
 */
public final class NodeRenderer extends RenderSystem {
	
	public NodeRenderer() {
		super(Node.class);
		setRenderPass(RenderPass.OVERLAY);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		Node node = chunk.getComponent(Node.class);
		
		if (node.currentInnerSize.x != 0 && node.currentInnerSize.y != 0 && node.backgroundColor != null) {
			renderer.drawRect(node.currentPosition.x, node.currentPosition.y, transform.position.z, node.currentInnerSize.x, node.currentInnerSize.y, node.backgroundColor);
		}
		
		if (node.getBorderWidth() > 0) {
			if (node.borderImage != null && node.borderImageSlice != null) {
				BorderImageRenderer.renderBorderImage(renderer, node);
			} else {
				node.updateBorderMesh();
				if (node.borderMesh != null) {
					node.borderMesh.draw(renderer, null, node.borderColor);
				}
			}
		}
		
		if (node.getOutlineWidth() > 0) {
			node.updateOutlineMesh();
			if (node.outlineMesh != null) {
				node.outlineMesh.draw(renderer, null, node.outlineColor);
			}
		}
	}
	
	@Override
	protected boolean isChunkActive(EntityChunk chunk) {
		return super.isChunkActive(chunk) && chunk.getComponent(Node.class).canBeRendered();
	}
}
