package dev.prozilla.pine.core.system.standard.ui.image;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public final class ImageRenderer extends RenderSystem {
	
	public ImageRenderer() {
		super(ImageNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		ImageNode imageNode = chunk.getComponent(ImageNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (!node.readyToRender) {
			return;
		}
		
		renderImage(renderer, imageNode, node, transform.getDepth());
	}
	
	public static void renderImage(Renderer renderer, ImageNode imageNode, Node node, float z) {
		renderImage(renderer, imageNode,
		 node.currentPosition.x + node.getPaddingX(), node.currentPosition.y + node.getPaddingY(), node.size.computeX(node), node.size.computeY(node), z, node.color);
	}
	
	public static void renderImage(Renderer renderer, ImageNode imageNode, float x, float y, float width, float height, float z, Color color) {
		renderImage(renderer, imageNode.image,
			imageNode.regionOffset.x, imageNode.regionOffset.y,
			imageNode.regionSize.x, imageNode.regionSize.y,
			x, y, width, height, z, color);
	}
	
	public static void renderImage(Renderer renderer, TextureAsset texture, float regX, float regY, float regWidth, float regHeight, float x, float y, float width, float height, float z, Color color) {
		float x2 = x + width;
		float y2 = y + height;
		
		float s1 = regX / texture.getWidth();
		float t1 = regY / texture.getHeight();
		float s2 = (regX + regWidth) / texture.getWidth();
		float t2 = (regY + regHeight) / texture.getHeight();
		
		if (color == null) {
			renderer.drawTextureRegion(texture, x, y, x2, y2, z, s1, t1, s2, t2);
		} else {
			renderer.drawTextureRegion(texture, x, y, x2, y2, z, s1, t1, s2, t2, color);
		}
	}
}
