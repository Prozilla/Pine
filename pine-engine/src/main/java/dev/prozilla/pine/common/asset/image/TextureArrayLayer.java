package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.Cloneable;

import java.util.Objects;

/**
 * Represents a layer of a texture array.
 * @see TextureArray
 */
public class TextureArrayLayer implements TextureAsset, Cloneable<TextureArrayLayer> {

	/** The path of the image of this texture */
	private final String path;
	/** The index of the layer of this texture in its texture array */
	private final int layer;
	private final TextureArray textureArray;
	
	public TextureArrayLayer(String path, TextureArray textureArray, int layer) {
		this.path = path;
		this.textureArray = textureArray;
		this.layer = layer;
	}
	
	@Override
	public void bind() {
		textureArray.bind();
	}
	
	@Override
	public void unbind() {
		textureArray.unbind();
	}
	
	@Override
	public void setParameter(int name, int value) {
		textureArray.setParameter(name, value);
	}
	
	@Override
	public boolean hasEqualLocation(TextureAsset other) {
		if (!other.isInArray()) {
			return false;
		}
		
		// Check if both textures are part of the same texture array
		TextureArray otherTextureArray = other.getArray();
		return textureArray.equals(otherTextureArray);
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
	public TextureArray getArray() {
		return textureArray;
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object || (object instanceof TextureArrayLayer textureArrayLayer && equals(textureArrayLayer));
	}
	
	@Override
	public boolean equals(TextureArrayLayer textureArrayLayer) {
		return textureArrayLayer != null && textureArrayLayer.getArray().equals(textureArray) && textureArrayLayer.layer == layer;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(textureArray.getId(), layer);
	}
	
	@Override
	public TextureArrayLayer clone() {
		return textureArray.addLayer(path);
	}
	
	/**
	 * Destroys the texture array.
	 */
	@Override
	public void destroy() {
		TextureAsset.super.destroy();
		textureArray.destroy();
	}
}
