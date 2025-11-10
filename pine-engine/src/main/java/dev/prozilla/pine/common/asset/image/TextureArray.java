package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.Application;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

/**
 * Represents a <a href="https://www.khronos.org/opengl/wiki/Array_Texture">OpenGL Array Texture</a>.
 * An Array Texture contains multiple images of the same size.
 */
public class TextureArray implements Cloneable<TextureArray>, Destructible, TextureBase {
	
	/** The handle of the texture array */
	private final int id;
	private final int width;
	private final int height;
	/** The amount of layers in this texture array */
	private final int layers;
	
	private final Map<Image, TextureArrayLayer> imageToLayer;
	/** The index of the next layer in this texture array */
	private int nextLayer;
	
	public static final int DEFAULT_LAYER_COUNT = 32;
	
	public TextureArray(int width, int height) {
		this(width, height, DEFAULT_LAYER_COUNT);
	}
	
	public TextureArray(int width, int height, int layers) {
		this(width, height, layers, Texture.DEFAULT_WRAP, Texture.DEFAULT_FILTER);
	}
	
	public TextureArray(int width, int height, int layers, Texture.Wrap wrap, Texture.Filter filter) {
		this.width = width;
		this.height = height;
		this.layers = layers;
		
		Application.requireOpenGL();
		
		id = glGenTextures();
		
		imageToLayer = new HashMap<>();
		nextLayer = 0;
		
		init(wrap, filter);
	}
	
	private void init(Texture.Wrap wrap, Texture.Filter filter) {
		bind();
		
		glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, layers);
		
		if (wrap != null) {
			setWrap(wrap);
		}
		if (filter != null) {
			setFilter(filter);
		}
		
		unbind();
	}
	
	@Override
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
	}
	
	@Override
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
	}
	
	@Override
	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D_ARRAY, name, value);
	}
	
	public TextureArrayLayer addLayer(String imagePath) {
		return addLayer(AssetPools.images.load(imagePath));
	}
	
	/**
	 * Adds a texture to this texture array in the next available layer.
	 * @throws IllegalStateException If all layers in this texture array are already being used.
	 * @throws IllegalArgumentException If the image has a different width or height than this texture array.
	 */
	public TextureArrayLayer addLayer(Image image) throws IllegalStateException, IllegalArgumentException {
		if (imageToLayer.containsKey(image)) {
			return imageToLayer.get(image);
		}
		
		if (nextLayer >= layers) {
			throw new IllegalStateException("exceeded size of texture array: " + layers);
		}
		if (image.getWidth() != width || image.getHeight() != height) {
			throw new IllegalArgumentException("image resolution does not match resolution of texture array");
		}
		
		// Upload image data to texture array
		bind();
		uploadData(image.getPixels());
		unbind();
		
		// Store image as layer
		TextureArrayLayer layer = new TextureArrayLayer(image.getPath(), this, nextLayer);
		imageToLayer.put(image, layer);
		
		nextLayer++;
		
		return layer;
	}
	
	public TextureArrayLayer getLayer(Image image) {
		return imageToLayer.get(image);
	}
	
	public boolean hasImage(Image image) {
		return imageToLayer.containsKey(image);
	}
	
	/**
	 * Checks if a given image can be added to this texture array.
	 */
	public boolean canAdd(Image image) {
		return nextLayer < layers && image.getWidth() == width && image.getHeight() == height;
	}
	
	private void uploadData(ByteBuffer pixels) {
		glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, nextLayer, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public Collection<TextureArrayLayer> getLayers() {
		return imageToLayer.values();
	}

	public int getUsedLayerCount() {
		return nextLayer;
	}
	
	public int getLayerCount() {
		return layers;
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object || (object instanceof TextureArray textureArray && equals(textureArray));
	}
	
	public boolean equals(TextureArray textureArray) {
		return textureArray != null && textureArray.getId() == id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public TextureArray clone() {
		return new TextureArray(width, height, layers);
	}
	
	/**
	 * Removes this texture array from the resource pool and deletes it.
	 */
	@Override
	public void destroy() {
		AssetPools.textures.removeTextureArray(this);
		glDeleteTextures(id);
	}
	
}
