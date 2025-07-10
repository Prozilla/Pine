package dev.prozilla.pine.common.asset.image;

/**
 * Represents a layer of a texture array.
 * @see TextureArray
 */
public class TextureArrayLayer implements TextureBase {

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
	public boolean hasEqualLocation(TextureBase other) {
		if (!other.isInArray()) {
			return false;
		}
		
		// Check if both textures are part of the same texture array
		TextureArray otherTextureArray = other.getArray();
		return otherTextureArray != null && textureArray.equals(otherTextureArray);
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
	
	/**
	 * Destroys the texture array.
	 */
	@Override
	public void destroy() {
		TextureBase.super.destroy();
		textureArray.destroy();
	}
}
