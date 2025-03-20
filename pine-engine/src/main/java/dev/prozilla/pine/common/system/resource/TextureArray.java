package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.core.Application;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

/**
 * Represents a <a href="https://www.khronos.org/opengl/wiki/Array_Texture">OpenGL Array Texture</a>.
 * An Array Texture contains multiple images of the same size.
 */
public class TextureArray {
	
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
		this.width = width;
		this.height = height;
		this.layers = layers;
		
		Application.requireOpenGL();
		
		id = glGenTextures();
		
		imageToLayer = new HashMap<>();
		nextLayer = 0;
		
		init();
	}
	
	private void init() {
		bind();
		
		glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, layers);
		
		setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		
		setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		unbind();
	}
	
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
	}
	
	private void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D_ARRAY, name, value);
	}
	
	public TextureArrayLayer addLayer(String imagePath) {
		return addLayer(ResourcePool.loadImage(imagePath));
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
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getRemainingLayers() {
		return layers - nextLayer;
	}
	
	public boolean equals(TextureArray other) {
		return other.getId() == id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public void destroy() {
		glDeleteTextures(id);
	}
	
}
