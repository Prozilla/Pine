package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.ResourceUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.stb.STBImage.*;

public final class ImagePool extends AssetPool<Image> {
	
	@Override
	public Image load(String path) {
		return super.load(path);
	}
	
	@Override
	protected Image createAsset(String path) {
		ByteBuffer imageBuffer;
		int width, height;
		int channels;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Prepare image buffers
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			// Get absolute path to image file
			String filePath;
			try {
				filePath = ResourceUtils.getResourcePath(path);
			} catch (RuntimeException e) {
				return fail(path, "File not found", e);
			}
			
			// Load the image file
			stbi_set_flip_vertically_on_load(true);
			imageBuffer = stbi_load(filePath, w, h, comp, 4);
			
			if (imageBuffer == null) {
				return fail(path, stbi_failure_reason(), null);
			}
			
			// Get image dimensions
			width = w.get();
			height = h.get();
			channels = comp.get();
		}
		
		return new Image(path, imageBuffer, width, height, channels);
	}
	
	/**
	 * Logs the amount of images in the resource pool per resolution.
	 */
	@Override
	public void printInfo(Logger logger) {
		super.printInfo(logger);
		
		// Calculate amount of images per resolution
		Map<Vector2i, Integer> resolutionToImageCount = new HashMap<>(count());
		for (Image image : getAssets()) {
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
	
}
