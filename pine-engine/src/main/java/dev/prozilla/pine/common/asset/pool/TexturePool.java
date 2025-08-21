package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.image.*;
import dev.prozilla.pine.common.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public final class TexturePool extends AssetPool<TextureBase> implements MultiAssetLoader<TextureBase> {
	
	private final ImagePool imagePool;
	private TextureArrayPolicy textureArrayPolicy;
	private final List<TextureArray> textureArrays;
	
	private static final TextureArrayPolicy DEFAULT_TEXTURE_ARRAY_POLICY = TextureArrayPolicy.SOMETIMES;
	
	public TexturePool(ImagePool imagePool) {
		this.imagePool = imagePool;
		textureArrayPolicy = DEFAULT_TEXTURE_ARRAY_POLICY;
		textureArrays = new ArrayList<>();
	}
	
	@Override
	public TextureBase load(String path) {
		return super.load(path);
	}
	
	public TextureBase loadInTextureArray(String path) {
		textureArrayPolicy = TextureArrayPolicy.ALWAYS;
		return load(path);
	}
	
	@Override
	protected TextureBase createAsset(String path) {
		Image image = imagePool.load(path);
		
		TextureBase texture = null;
		
		// Look for available texture array to load texture into
		if (textureArrayPolicy.canUseArray()) {
			for (TextureArray textureArray : textureArrays) {
				if (textureArray.hasImage(image)) {
					texture = textureArray.getLayer(image);
					break;
				} else if (textureArray.canAdd(image)) {
					texture = textureArray.addLayer(image);
					break;
				}
			}
		}
		
		// Create a new texture array and add texture
		if (texture == null && textureArrayPolicy.canCreateArray()) {
			try {
				TextureArray textureArray = createTextureArray(image.getWidth(), image.getHeight());
				texture = textureArray.addLayer(image);
			} catch (RuntimeException e) {
				fail(path, "Failed to create texture array", e);
			}
		}
		
		// Fall back to standard texture
		if (texture == null) {
			texture = new Texture(image);
		}
		return texture;
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		textureArrayPolicy = DEFAULT_TEXTURE_ARRAY_POLICY;
	}
	
	@Override
	protected boolean remove(String path) {
		boolean removed = super.remove(path);
		if (removed) {
			imagePool.remove(path);
		}
		return removed;
	}
	
	/**
	 * Removes a texture array and all its textures from the resource pool.
	 * @param textureArray Texture array to remove
	 */
	public boolean removeTextureArray(TextureArray textureArray) {
		if (!textureArrays.contains(textureArray)) {
			return false;
		}
		
		for (TextureArrayLayer layer : textureArray.getLayers()) {
			String path = layer.getPath();
			if (path != null) {
				remove(path);
			}
		}
		
		return textureArrays.remove(textureArray);
	}
	
	/**
	 * Creates a texture array that can be used to load multiple textures with the same resolution into.
	 * @param width The width of the textures
	 * @param height The height of the textures
	 */
	public TextureArray createTextureArray(int width, int height) {
		return createTextureArray(width, height, TextureArray.DEFAULT_LAYER_COUNT);
	}
	
	/**
	 * Creates a texture array that can be used to load multiple textures with the same resolution into.
	 * @param width The width of the textures
	 * @param height The height of the textures
	 * @param layers The amount of textures to fit into the texture array
	 */
	public TextureArray createTextureArray(int width, int height, int layers) {
		TextureArray textureArray = new TextureArray(width, height, layers);
		textureArrays.add(textureArray);
		return textureArray;
	}
	
	/**
	 * Clears the pool of texture arrays.
	 */
	public void clearTextureArrays() {
		TextureArray[] textureArraysToDestroy = textureArrays.toArray(new TextureArray[0]);
		textureArrays.clear();
		for (TextureArray textureArray : textureArraysToDestroy) {
			textureArray.destroy();
		}
	}
	
	/**
	 * Returns the amount of texture arrays in this pool.
	 * @return The amount of texture arrays in this pool.
	 */
	public int textureArrayCount() {
		return textureArrays.size();
	}
	
	@Override
	public void printInfo(Logger logger) {
		super.printInfo(logger);
		printTextureArraysInfo(logger);
	}
	
	/**
	 * Logs the dimensions and layer usage of every texture array in the resource pool.
	 */
	public void printTextureArraysInfo(Logger logger) {
		logger.log("Texture arrays: " + textureArrayCount());
		
		for (TextureArray textureArray : textureArrays) {
			logger.logf("%sx%s texture array: %s/%s layers used", textureArray.getWidth(), textureArray.getHeight(),
				textureArray.getUsedLayerCount(), textureArray.getLayerCount());
		}
	}
	
}
