package dev.prozilla.pine.common.system.resource;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

public class TextureArray {
	
	private final int id;
	private final int width;
	private final int height;
	private final int layers;
	
	private final Map<Image, TextureArrayLayer> imageToLayer;
	private int nextLayer;
	
	public static final int DEFAULT_LAYER_COUNT = 32;
	
	public TextureArray(int width, int height) {
		this(width, height, DEFAULT_LAYER_COUNT);
	}
	
	public TextureArray(int width, int height, int layers) {
		this.width = width;
		this.height = height;
		this.layers = layers;
		
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
	
	public TextureArrayLayer addTexture(String imagePath) {
		return addTexture(ResourcePool.loadImage(imagePath));
	}
	
	public TextureArrayLayer addTexture(Image image) {
		if (imageToLayer.containsKey(image)) {
			return imageToLayer.get(image);
		}
		
		if (nextLayer >= layers) {
			throw new IllegalStateException("exceeded size of texture array: " + layers);
		}
		if (image.getWidth() != width || image.getHeight() != height) {
			throw new IllegalArgumentException("image resolution does not match resolution of texture array");
		}
		
		bind();
		uploadData(image.getPixels());
		TextureArrayLayer layer = new TextureArrayLayer(image.getPath(), this, nextLayer);
		imageToLayer.put(image, layer);
		
		nextLayer++;
		
		return layer;
	}
	
	public TextureArrayLayer getTexture(Image image) {
		return imageToLayer.get(image);
	}
	
	public boolean hasImage(Image image) {
		return imageToLayer.containsKey(image);
	}
	
	public boolean canAdd(Image image) {
		return nextLayer < layers && image.getWidth() == width && image.getHeight() == height;
	}
	
	public void uploadData(ByteBuffer pixels) {
		uploadData(pixels, GL_RGBA);
	}
	
	public void uploadData(ByteBuffer pixels, int format) {
		glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, nextLayer, width, height, 1, format, GL_UNSIGNED_BYTE, pixels);
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
	
	public void destroy() {
		glDeleteTextures(id);
	}
	
}
