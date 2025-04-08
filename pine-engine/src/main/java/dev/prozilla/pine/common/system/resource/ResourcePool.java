package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.resource.image.*;
import dev.prozilla.pine.common.system.resource.image.Image;
import dev.prozilla.pine.common.system.resource.text.Font;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

import static org.lwjgl.stb.STBImage.*;

/**
 * Represents a pool that manages resources efficiently,
 * by avoiding loading resources multiple times.
 */
public final class ResourcePool {
	
	private static final Map<String, TextureBase> textures = new HashMap<>();
	private static final Map<String, Image> images = new HashMap<>();
	private static final Map<String, Font> fonts = new HashMap<>();
	private static final Map<String, StyleSheet> styleSheets = new HashMap<>();
	
	private static final List<TextureArray> textureArrays = new ArrayList<>();
	
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
			
			Logger.system.logPath("Loading image", filePath);
			
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
		
		Image image = new Image(path, imageBuffer, width, height, channels);
		images.put(path, image);
		return image;
	}
	
	public static boolean removeImage(String path) {
		return images.remove(normalizePath(path)) != null;
	}
	
	/**
	 * Clears the image pool.
	 */
	public static void clearImages() {
		clearResource(images);
	}
	
	/**
	 * Loads a texture from the resource pool or file system.
	 * If the texture has not been pooled yet, it will be loaded into a texture array.
	 * @param path Path of the texture's image file
	 * @throws RuntimeException If the image file fails to load.
	 * @throws IllegalStateException If OpenGL hasn't been initialized yet.
	 */
	public static TextureBase loadTextureInArray(String path) throws RuntimeException {
		return loadTexture(path, TextureArrayPolicy.ALWAYS);
	}
	
	/**
	 * Loads a texture from the resource pool or file system.
	 * @param path Path of the texture's image file
	 * @throws RuntimeException If the image file fails to load.
	 * @throws IllegalStateException If OpenGL hasn't been initialized yet.
	 */
	public static TextureBase loadTexture(String path) throws RuntimeException {
		return loadTexture(path, TextureArrayPolicy.SOMETIMES);
	}
	
	/**
	 * Loads a texture from the resource pool or file system.
	 * @param path Path of the texture's image file
	 * @param textureArrayPolicy Policy that determines when to load the texture into a texture array.
	 * @throws RuntimeException If the image file fails to load.
	 * @throws IllegalStateException If OpenGL hasn't been initialized yet.
	 */
	public static TextureBase loadTexture(String path, TextureArrayPolicy textureArrayPolicy) throws IllegalStateException, RuntimeException {
		path = PathUtils.removeLeadingSlash(path);
		
		if (textures.containsKey(path)) {
			return textures.get(path);
		}
		
		Logger.system.logf("Loading texture: %s", path);
		
		Image image = loadImage(path);
		
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
			TextureArray textureArray = createTextureArray(image.getWidth(), image.getHeight());
			texture = textureArray.addLayer(image);
		}
		
		// Fall back to standard texture
		if (texture == null) {
			texture = new Texture(image);
		}
		
		textures.put(path, texture);
		return texture;
	}
	
	/**
	 * Removes a texture and its corresponding image from the resource pool.
	 * @param path Path of the texture.
	 */
	public static boolean removeTexture(String path) {
		boolean removed = textures.remove(normalizePath(path)) != null;
		
		if (removed) {
			removeImage(path);
		}
		
		return removed;
	}
	
	/**
	 * Creates a texture array that can be used to load multiple textures with the same resolution into.
	 * @param width The width of the textures
	 * @param height The height of the textures
	 */
	public static TextureArray createTextureArray(int width, int height) {
		return createTextureArray(width, height, TextureArray.DEFAULT_LAYER_COUNT);
	}
	
	/**
	 * Removes a texture array and all its textures from the resource pool.
	 * @param textureArray Texture array to remove
	 */
	public static boolean removeTextureArray(TextureArray textureArray) {
		if (!textureArrays.contains(textureArray)) {
			return false;
		}
		
		for (TextureArrayLayer layer : textureArray.getLayers()) {
			String path = layer.getPath();
			if (path != null) {
				removeTexture(path);
			}
		}
		
		return textureArrays.remove(textureArray);
	}
	
	/**
	 * Creates a texture array that can be used to load multiple textures with the same resolution into.
	 * @param width The width of the textures
	 * @param height The height of the textures
	 * @param layers The amount of textures to fit into the texture array
	 */
	public static TextureArray createTextureArray(int width, int height, int layers) {
		TextureArray textureArray = new TextureArray(width, height, layers);
		textureArrays.add(textureArray);
		return textureArray;
	}
	
	/**
	 * Clears the texture pool.
	 */
	public static void clearTextures() {
		clearResource(textures);
	}
	
	/**
	 * Clears the pool of texture arrays.
	 */
	public static void clearTextureArrays() {
		TextureArray[] textureArraysToDestroy = textureArrays.toArray(new TextureArray[0]);
		textureArrays.clear();
		for (TextureArray textureArray : textureArraysToDestroy) {
			textureArray.destroy();
		}
	}
	
	public static Font loadFont(String path) {
		return loadFont(path, 16);
	}
	
	public static Font loadFont(String path, int size) {
		path = PathUtils.addLeadingSlash(path);
		String key = Font.generateKey(path, size);
		
		if (fonts.containsKey(key)) {
			return fonts.get(key);
		}
		
		Logger.system.logPath(String.format("Loading font (size: %s)", size), ResourceUtils.getResourcePath(path));
		
		Font font;
		try {
			InputStream stream = ResourcePool.class.getResourceAsStream(path);
			font = new Font(stream, size);
			font.path = path;
		} catch (FontFormatException | IOException e) {
			Logger.system.error("Failed to load font: " + path, e);
			font = new Font();
		}
		
		fonts.put(key, font);
		return font;
	}
	
	public static boolean removeFont(String path, int size) {
		String key = Font.generateKey(path, size);
		return fonts.remove(key) != null;
	}
	
	public static void clearFonts() {
		clearResource(fonts);
	}
	
	public static StyleSheet loadStyleSheet(String path) {
		path = PathUtils.addLeadingSlash(path);
		
		if (styleSheets.containsKey(path)) {
			return styleSheets.get(path);
		}
		
		Logger.system.logPath("Loading style sheet", ResourceUtils.getResourcePath(path));
		
		StyleSheet styleSheet;
		try (InputStream inputStream = Objects.requireNonNull(ResourcePool.class.getResourceAsStream(path));
		     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			
			while (line != null) {
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
				line = bufferedReader.readLine();
			}
			
			styleSheet = StyleSheet.parse(stringBuilder.toString());
		} catch (IOException | NullPointerException e) {
			Logger.system.error("Failed to load style sheet: " + path, e);
			styleSheet = new StyleSheet();
		}
		
		styleSheets.put(path, styleSheet);
		return styleSheet;
	}
	
	public static boolean removeStyleSheet(String path) {
		return styleSheets.remove(PathUtils.addLeadingSlash(path)) != null;
	}
	
	public static void clearStyleSheets() {
		clearResource(styleSheets);
	}
	
	private static <R extends Resource> void clearResource(Map<String, R> resources) {
		Collection<R> resourcesToDestroy = resources.values();
		resources.clear();
		for (Resource resource : resourcesToDestroy) {
			resource.destroy();
		}
	}
	
	/**
	 * Clears the resource pool.
	 */
	public static void clear() {
		clearImages();
		clearTextureArrays();
		clearTextures();
		clearFonts();
		clearStyleSheets();
	}
	
	/**
	 * Returns a normalized file path.
	 * @param path Original path
	 * @return Normalized path
	 */
	private static String normalizePath(String path) {
		return PathUtils.removeLeadingSlash(path);
	}
	
	/**
	 * Logs the dimensions and layer usage of every texture array in the resource pool.
	 */
	public static void printTextureArrayStats(Logger logger) {
		for (TextureArray textureArray : textureArrays) {
			logger.logf("%sx%s texture array: %s/%s layers used", textureArray.getWidth(), textureArray.getHeight(),
				textureArray.getUsedLayerCount(), textureArray.getLayerCount());
		}
	}
	
	/**
	 * Logs amount of images in the resource pool per resolution.
	 */
	public static void printImageStats(Logger logger) {
		logger.log("Total images: " + getImageCount());
		
		// Calculate amount of images per resolution
		Map<Vector2i, Integer> resolutionToImageCount = new HashMap<>(getImageCount());
		for (Image image : images.values()) {
			Vector2i resolution = new Vector2i(image.getWidth(), image.getHeight());
			resolutionToImageCount.put(resolution, resolutionToImageCount.getOrDefault(resolution, 0) + 1);
		}
		
		// Format and log results
		resolutionToImageCount.entrySet().stream()
			.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
			.forEach((entry) -> {
				Vector2i resolution = entry.getKey();
				int count = entry.getValue();
				logger.logf("%sx%s images: %s", resolution.x, resolution.y, count);
			});
	}
	
	/**
	 * Logs the current amounts of different types of resources in the resource pool.
	 */
	public static void printStats(Logger logger) {
		logger.log("Images in resource pool: " + getImageCount());
		logger.log("Textures in resource pool: " + getTextureCount());
		logger.log("Texture arrays in resource pool: " + getTextureArrayCount());
		logger.log("Fonts in resource pool: " + getFontCount());
		logger.log("Style sheets in resource pool: " + getStyleSheetCount());
	}
	
	public static int getImageCount() {
		return images.size();
	}
	
	public static int getTextureCount() {
		return textures.size();
	}
	
	public static int getTextureArrayCount() {
		return textureArrays.size();
	}
	
	public static int getFontCount() {
		return fonts.size();
	}
	
	public static int getStyleSheetCount() {
		return styleSheets.size();
	}
	
}
