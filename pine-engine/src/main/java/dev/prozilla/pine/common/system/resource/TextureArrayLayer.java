package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.core.rendering.Renderer;

public class TextureArrayLayer implements TextureBase {

	private final String path;
	private final int layer;
	private final TextureArray textureArray;
	
	public TextureArrayLayer(String path, TextureArray textureArray, int layer) {
		this.path = path;
		this.textureArray = textureArray;
		this.layer = layer;
	}
	
	@Override
	public void bind() {
		if (true || Texture.currentTextureId == null || Texture.currentTextureId != layer || !Renderer.usingTextureArray) {
			textureArray.bind();
			Texture.currentTextureId = layer;
			Renderer.usingTextureArray = true;
		}
	}
	
	@Override
	public void unbind() {
		textureArray.unbind();
	}
	
	@Override
	public int getId() {
		return layer;
	}
	
	@Override
	public int getWidth() {
		return textureArray.getWidth();
	}
	
	@Override
	public int getHeight() {
		return textureArray.getHeight();
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public boolean isInArray() {
		return true;
	}
	
	@Override
	public void destroy() {
		textureArray.destroy();
	}
}
