package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.system.Path;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.Application;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.stb.STBImage.*;

/**
 * Represents a pool that manages resources efficiently,
 * by avoiding loading resources multiple times.
 */
public final class ResourcePool {
	
	private static final Map<String, Texture> textures = new HashMap<>();
	private final static Map<String, Image> images = new HashMap<>();
	private final static Map<String, Font> fonts = new HashMap<>();
	
	/**
	 * Loads an image from the resource pool or file system.
	 * @param path Path of the image file
	 * @return Image
	 * @throws RuntimeException If the image file fails to load.
	 */
	public static Image loadImage(String path) throws RuntimeException {
		path = normalizePath(path);
		
		if (images.containsKey(path)) {
			return images.get(path);
		}
		
		ByteBuffer imageBuffer;
		int width, height;
		int channels;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Prepare image buffers
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			// Get absolute path to image file
			String filePath = ResourceUtils.getResourcePath(path);
			
			Logger.system.logFile("Loading image", filePath);
			
			// Load the image file
			stbi_set_flip_vertically_on_load(true);
			imageBuffer = stbi_load(filePath, w, h, comp, 4);
			
			if (imageBuffer == null) {
				throw new RuntimeException("Failed to load image file"
					+ System.lineSeparator() + stbi_failure_reason());
			}
			
			// Get image dimensions
			width = w.get();
			height = h.get();
			channels = comp.get();
		}
		
		Image image = new Image(imageBuffer, width, height, channels);
		images.put(path, image);
		return image;
	}
	
	/**
	 * Clears the image pool.
	 */
	private static void clearImages() {
		for (Image image : images.values()) {
			image.destroy();
		}
		images.clear();
	}
	
	/**
	 * Loads a texture from the resource pool or file system.
	 * @param path Path of the texture's image file
	 * @return Texture
	 * @throws RuntimeException If the image file fails to load.
	 * @throws RuntimeException If OpenGL hasn't been initialized yet.
	 */
	public static Texture loadTexture(String path) throws RuntimeException {
		path = Path.removeLeadingSlash(path);
		
		if (textures.containsKey(path)) {
			return textures.get(path);
		}
		
		if (!Application.initializedOpenGL) {
			throw new RuntimeException("Can't load textures before initialization");
		}
		
		Logger.system.logFile("Loading texture", path);
		
		// Create texture from image
		Image image = loadImage(path);
		Texture texture = Texture.createTexture(image);
		
		textures.put(path, texture);
		return texture;
	}
	
	/**
	 * Clears the texture pool.
	 */
	private static void clearTextures() {
		for (Texture texture : textures.values()) {
			texture.destroy();
		}
		textures.clear();
	}
	
	public static Font loadFont(String path) {
		return loadFont(path, 16);
	}
	
	public static Font loadFont(String path, int size) {
		path = "/" + normalizePath(path);
		String key = Font.generateKey(path, size);
		
		if (fonts.containsKey(key)) {
			return fonts.get(key);
		}
		
		Logger.system.logFile(String.format("Loading font (size: %s)", size), path);
		
		Font font;
		try {
			InputStream stream = ResourcePool.class.getResourceAsStream(path);
			font = new Font(stream, size);
		} catch (FontFormatException | IOException e) {
			Logger.system.error("Failed to load font: " + path, e);
			font = new Font();
		}
		
		fonts.put(key, font);
		return font;
	}
	
	private static void clearFonts() {
		for (Font font : fonts.values()) {
			font.destroy();
		}
		fonts.clear();
	}
	
	/**
	 * Clears the resource pool.
	 */
	public static void clear() {
		clearImages();
		clearTextures();
		clearFonts();
	}
	
	/**
	 * Returns a normalized file path.
	 * @param path Original path
	 * @return Normalized path
	 */
	private static String normalizePath(String path) {
		return Path.removeLeadingSlash(path);
	}
}
