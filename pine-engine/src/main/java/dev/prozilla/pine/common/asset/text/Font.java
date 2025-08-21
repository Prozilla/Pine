package dev.prozilla.pine.common.asset.text;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Contains a font texture for drawing text.
 *
 * <p>Font textures are created using <a href="https://github.com/nothings/stb/blob/f1c79c02822848a9bed4315b12c8c8f3761e1296/stb_truetype.h">stb_truetype</a></p>
 */
public class Font implements Asset {
	
	/** The glyphs for each char. */
	private final Map<Character, Glyph> glyphs;
	private final Texture texture;
	
	/** Height of the font. */
	private float fontHeight;
	private float fontDescent;
	private final int size;
	public String path;
	
	public static final int DEFAULT_SIZE = 16;
	public static final Color DEFAULT_COLOR = Color.white();
	
	public static final int FIRST_CHAR = 32;
	public static final int CHAR_COUNT = 224;
	public static final int DEL_CHAR = 127;
	
	/**
	 * Creates a font from a TTF input stream.
	 */
	public Font(InputStream in, int size) throws IOException {
		this(in, size, true);
	}
	
	/**
	 * Creates a font from a TTF input stream.
	 */
	public Font(InputStream in, int size, boolean antiAlias) throws IOException {
		glyphs = new HashMap<>();
		this.size = size;
		this.texture = createFontTexture(in, size);
	}
	
	public Font(int size) {
		try (InputStream defaultFont = Font.class.getResourceAsStream("/fonts/Inconsolata.ttf")) {
			if (defaultFont == null) {
				throw new RuntimeException("Default font missing.");
			}
			glyphs = new HashMap<>();
			this.size = size;
			this.texture = createFontTexture(defaultFont, size);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a font texture from a given input stream.
	 * @param fontStream The input stream
	 * @param fontSize The size of the font
	 * @return The font texture
	 */
	private Texture createFontTexture(InputStream fontStream, int fontSize) throws IOException {
		ByteBuffer fontBuffer = null;
		try {
			// Load font bytes
			byte[] fontBytes = fontStream.readAllBytes();
			fontBuffer = memAlloc(fontBytes.length);
			fontBuffer.put(fontBytes).flip();
			
			int bitmapWidth = 512;
			int bitmapHeight = 512;
			ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
			
			// Bake ASCII glyphs into a bitmap
			STBTTBakedChar.Buffer characterData = STBTTBakedChar.malloc(CHAR_COUNT);
			stbtt_BakeFontBitmap(fontBuffer, fontSize, bitmap, bitmapWidth, bitmapHeight, FIRST_CHAR, characterData);
			
			ByteBuffer pixels = createPixelData(bitmapWidth, bitmapHeight, bitmap);
			Texture texture = new Texture(null, bitmapWidth, bitmapHeight, pixels);
			
			STBTTFontinfo fontInfo = STBTTFontinfo.malloc();
			if (!stbtt_InitFont(fontInfo, fontBuffer)) {
				throw new IOException("Failed to initialize font");
			}
			
			// Get vertical metrics for proper line height
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer ascent = stack.mallocInt(1);
				IntBuffer descent = stack.mallocInt(1);
				IntBuffer lineGap = stack.mallocInt(1);
				
				stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);
				
				// Scale to pixel height
				float scale = stbtt_ScaleForPixelHeight(fontInfo, fontSize);
				fontHeight = (ascent.get(0) - descent.get(0) + lineGap.get(0)) * scale;
				fontDescent = descent.get(0) * scale;
				
				// Measure each character’s quad size
				FloatBuffer xPos = stack.floats(0.0f);
				FloatBuffer yPos = stack.floats(0.0f);
				STBTTAlignedQuad quad = STBTTAlignedQuad.malloc(stack);
				
				for (int i = 0; i < CHAR_COUNT; i++) {
					createGlyph(i, xPos, yPos, characterData, bitmapWidth, bitmapHeight, quad);
				}
			}
			
			characterData.free();
			fontInfo.free();
			return texture;
		} finally {
			if (fontBuffer != null) {
				memFree(fontBuffer);
			}
		}
	}
	
	/**
	 * Creates a glyph of a given character and stores it.
	 * @param i The index of the character in the character data array
	 * @param xPos The current x position
	 * @param yPos The current y position
	 * @param characterData The character data array
	 * @param bitmapWidth The width of the bitmap
	 * @param bitmapHeight The height of the bitmap
	 * @param quad The quad struct to store the character metrics in
	 */
	private void createGlyph(int i, FloatBuffer xPos, FloatBuffer yPos, STBTTBakedChar.Buffer characterData, int bitmapWidth, int bitmapHeight, STBTTAlignedQuad quad) {
		int code = FIRST_CHAR + i;
		if (code == DEL_CHAR) {
			return;
		}
		char character = (char)code;
		
		float startX = xPos.get(0);
		stbtt_GetBakedQuad(characterData, bitmapWidth, bitmapHeight, i, xPos, yPos, quad, true);
		float endX = xPos.get(0);
		
		float regionX = quad.s0() * bitmapWidth;
		float regionY = (1f - quad.t1()) * bitmapHeight;
		float regionWidth  = (quad.s1() - quad.s0()) * bitmapWidth;
		float regionHeight = (quad.t1() - quad.t0()) * bitmapHeight;
		
		float y = quad.y1();
		float height = quad.y1() - quad.y0();
		float advance = endX - startX;
		
		Glyph glyph = new Glyph(regionWidth, regionHeight, regionX, regionY, y, height, advance);
		glyphs.put(character, glyph);
	}
	
	/**
	 * Calculates the width of a character sequence in this font.
	 * @param text The character sequence
	 * @return The width of the character sequence.
	 */
	public float getWidth(CharSequence text) {
		float width = 0;
		float lineWidth = 0;
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '\n') {
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				continue;
			}
			if (character == '\r') {
				continue;
			}
			Glyph glyph = glyphs.get(character);
			if (glyph != null) {
				lineWidth += glyph.advance;
			}
		}
		return Math.max(width, lineWidth);
	}
	
	/**
	 * Calculates the height of a character sequence in this font.
	 * @param text The character sequence
	 * @return The height of the character sequence.
	 */
	public float getHeight(CharSequence text) {
		float height = 0;
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '\n') {
				height += fontHeight;
			}
		}
		height += fontHeight;
		return height;
	}
	
	public int getSize() {
		return size;
	}
	
	/**
	 * Draws text on the screen at the given coordinates with the default text color using this font.
	 * @param renderer The renderer to use
	 * @param text The text to draw
	 * @param x The x position
	 * @param y The y position
	 * @param z The z position
	 */
	public void drawText(Renderer renderer, CharSequence text, float x, float y, float z) {
		drawText(renderer, text, x, y, z, DEFAULT_COLOR);
	}
	
	/**
	 * Draws text on the screen at the given coordinates using this font.
	 * @param renderer The renderer to use
	 * @param text The text to draw
	 * @param x The x position
	 * @param y The y position
	 * @param z The z position
	 * @param c The color to draw the text in
	 */
	public void drawText(Renderer renderer, CharSequence text, float x, float y, float z, Color c) {
		float drawX = x;
		float drawY = y;
		
		float textHeight = getHeight(text);
		if (textHeight > fontHeight) {
			drawY += textHeight - fontHeight;
		}
		
		drawY -= fontDescent;
		
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '\n') {
				drawY -= fontHeight;
				drawX = x;
				continue;
			}
			if (character == '\r') {
				continue;
			}
			Glyph glyph = glyphs.get(character);
			if (glyph != null) {
				renderer.drawTextureRegion(texture, drawX, drawY - glyph.y, z, glyph.regionX, glyph.regionY, glyph.regionWidth, glyph.regionHeight, c);
				drawX += glyph.advance;
			}
		}
	}
	
	/**
	 * Creates a new font from the same font file, but with a different size.
	 */
	public Font setSize(int size) {
		return AssetPools.fonts.load(path, size);
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	/**
	 * Deletes the font.
	 */
	@Override
	public void destroy() {
		String path = getPath();
		if (path != null) {
			AssetPools.fonts.remove(this);
		}
		texture.destroy();
	}
	
	public static String generateKey(String path, int size) {
		return String.format("%s:%s", path, size);
	}
	
	/**
	 * Converts a bitmap to a buffer with RGBA values for each pixel.
	 * @param bitmapWidth The width of the bitmap
	 * @param bitmapHeight The height of the bitmap
	 * @param bitmap The bitmap
	 * @return The buffer containing RGBA values for each pixel
	 */
	private static @NotNull ByteBuffer createPixelData(int bitmapWidth, int bitmapHeight, ByteBuffer bitmap) {
		ByteBuffer rgba = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight * 4);
		for (int y = 0; y < bitmapHeight; y++) {
			for (int x = 0; x < bitmapWidth; x++) {
				int source = y * bitmapWidth + x;
				int destination = (bitmapHeight - 1 - y) * bitmapWidth + x;
				
				byte alpha = bitmap.get(source);
				
				rgba.put(destination * 4, (byte)0xFF);
				rgba.put(destination * 4 + 1, (byte)0xFF);
				rgba.put(destination * 4 + 2, (byte)0xFF);
				rgba.put(destination * 4 + 3, alpha);
			}
		}
		rgba.flip();
		return rgba;
	}
	
}