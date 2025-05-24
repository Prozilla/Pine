package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.BorderImage;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;
import dev.prozilla.pine.core.system.standard.ui.image.ImageRenderer;

public class BorderImageRenderer extends RenderSystem {
	
	public BorderImageRenderer() {
		super(Node.class, BorderImage.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		Node node = chunk.getComponent(Node.class);
		BorderImage borderImage = chunk.getComponent(BorderImage.class);
		
		renderBorderImage(renderer, borderImage.texture, node, borderImage.slice, borderImage.fill, transform.getDepth());
	}
	
	public static void renderBorderImage(Renderer renderer, Node node, float z) {
		renderBorderImage(renderer, node.borderImage, node, node.borderImageSlice, node.borderImageSliceFill, z);
	}
	
	public static void renderBorderImage(Renderer renderer, TextureBase texture, Node node, Vector4f slice, boolean fill, float z) {
		float borderWidth = node.getBorderWidth();
		
		float nodeX = node.currentPosition.x;
		float nodeY = node.currentPosition.y;
		float nodeWidth = node.currentInnerSize.x;
		float nodeHeight = node.currentInnerSize.y;
		
		float textureWidth = texture.getWidth();
		float textureHeight = texture.getHeight();
		
		float left = slice.x;
		float right = slice.y;
		float top = slice.z;
		float bottom = slice.w;
		
		Color color = node.color;
		
		// Texture slice regions
		float textureLeft = textureWidth * left;
		float textureRight = textureWidth * right;
		float textureTop = textureHeight * top;
		float textureBottom = textureHeight * bottom;
		
		// Top-left
		ImageRenderer.renderImage(renderer, texture,
			0, 0, textureLeft, textureBottom,
			nodeX, nodeY, borderWidth, borderWidth, z, color);
		
		// Top
		ImageRenderer.renderImage(renderer, texture,
			textureLeft, 0, textureWidth - textureLeft - textureRight, textureBottom,
			nodeX + borderWidth, nodeY, nodeWidth - borderWidth - borderWidth, borderWidth, z, color);
		
		// Top-right
		ImageRenderer.renderImage(renderer, texture,
			textureWidth - textureRight, 0, textureRight, textureBottom,
			nodeX + nodeWidth - borderWidth, nodeY, borderWidth, borderWidth, z, color);
		
		// Left
		ImageRenderer.renderImage(renderer, texture,
			0, textureBottom, textureLeft, textureHeight - textureTop - textureBottom,
			nodeX, nodeY + borderWidth, borderWidth, nodeHeight - borderWidth - borderWidth, z, color);
		
		// Center
		if (fill) {
			ImageRenderer.renderImage(renderer, texture,
				textureLeft, textureBottom, textureWidth - textureLeft - textureRight, textureHeight - textureTop - textureBottom,
				nodeX + borderWidth, nodeY + borderWidth, nodeWidth - borderWidth - borderWidth, nodeHeight - borderWidth - borderWidth, z, color);
		}
		
		// Right
		ImageRenderer.renderImage(renderer, texture,
			textureWidth - textureRight, textureBottom, textureRight, textureHeight - textureTop - textureBottom,
			nodeX + nodeWidth - borderWidth, nodeY + borderWidth, borderWidth, nodeHeight - borderWidth - borderWidth, z, color);
		
		// Bottom-left
		ImageRenderer.renderImage(renderer, texture,
			0, textureHeight - textureTop, textureLeft, textureTop,
			nodeX, nodeY + nodeHeight - borderWidth, borderWidth, borderWidth, z, color);
		
		// Bottom
		ImageRenderer.renderImage(renderer, texture,
			textureLeft, textureHeight - textureTop, textureWidth - textureLeft - textureRight, textureTop,
			nodeX + borderWidth, nodeY + nodeHeight - borderWidth, nodeWidth - borderWidth - borderWidth, borderWidth, z, color);
		
		// Bottom-right
		ImageRenderer.renderImage(renderer, texture,
			textureWidth - textureRight, textureHeight - textureTop, textureRight, textureTop,
			nodeX + nodeWidth - borderWidth, nodeY + nodeHeight - borderWidth, borderWidth, borderWidth, z, color);
	}
}
