package dev.prozilla.pine.common.asset.text;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.RectPacker;
import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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
	private float lineHeight;
	private float descent;
	public String path;
	
	public static final int DEFAULT_SIZE = 16;
	public final static String DEFAULT_FONT_PATH = "/fonts/Rubik.ttf";
	
	public static final int FIRST_CHAR = 32;
	public static final int CHAR_COUNT = 224;
	public static final int DEL_CHAR = 127;
	
	/** The pixel height at which glyphs are generated. Text of any other size is scaled from this. */
	public static final int BASE_SIZE = 32;
	/** The amount of texels around each glyph that store distance information. */
	public static final int GLYPH_PADDING = Texture.SIGNED_DISTANCE_FIELD_PADDING;
	public static final int ATLAS_SIZE = 1024;
	private static final int ATLAS_MARGIN = 1;
	
	public Font() {
		try (InputStream inputStream = Font.class.getResourceAsStream(DEFAULT_FONT_PATH)) {
			if (inputStream == null) {
				throw new RuntimeException("Default font missing.");
			}
			glyphs = new HashMap<>();
			this.texture = createFontTexture(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a font from a TTF input stream.
	 */
	public Font(InputStream in) throws IOException {
		glyphs = new HashMap<>();
		this.texture = createFontTexture(in);
	}
	
	/**
	 * Creates a font texture from a given input stream.
	 * @param fontStream The input stream
	 * @return The font texture
	 */
	private Texture createFontTexture(InputStream fontStream) throws IOException {
		ByteBuffer fontBuffer = null;
		STBTTFontinfo fontInfo = STBTTFontinfo.malloc();
		try {
			byte[] fontBytes = fontStream.readAllBytes();
			fontBuffer = memAlloc(fontBytes.length);
			fontBuffer.put(fontBytes).flip();
			
			if (!stbtt_InitFont(fontInfo, fontBuffer)) {
				throw new IOException("Failed to initialize font");
			}
			
			float scale = stbtt_ScaleForPixelHeight(fontInfo, BASE_SIZE);
			
			// Get vertical metrics for line height
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer ascentBuffer = stack.mallocInt(1);
				IntBuffer descentBuffer = stack.mallocInt(1);
				IntBuffer lineGapBuffer = stack.mallocInt(1);
				
				stbtt_GetFontVMetrics(fontInfo, ascentBuffer, descentBuffer, lineGapBuffer);
				
				lineHeight = (ascentBuffer.get(0) - descentBuffer.get(0) + lineGapBuffer.get(0)) * scale;
				descent = descentBuffer.get(0) * scale;
			}
			
			return createSignedDistanceFieldTexture(fontInfo, scale);
		} finally {
			fontInfo.free();
			if (fontBuffer != null) {
				memFree(fontBuffer);
			}
		}
	}
	
	/**
	 * Generates a signed distance field for each glyph, packs them into an atlas and creates a glyph for each character.
	 * @param fontInfo The font info
	 * @param scale The scale to generate the glyphs at
	 * @return The atlas texture
	 */
	private Texture createSignedDistanceFieldTexture(STBTTFontinfo fontInfo, float scale) throws IOException {
		ByteBuffer alphaMask = BufferUtils.createByteBuffer(ATLAS_SIZE * ATLAS_SIZE);
		RectPacker packer = new RectPacker(ATLAS_SIZE, ATLAS_SIZE, ATLAS_MARGIN);
		float distanceScale = Texture.SIGNED_DISTANCE_FIELD_SCALE;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			IntBuffer offsetXBuffer = stack.mallocInt(1);
			IntBuffer offsetYBuffer = stack.mallocInt(1);
			IntBuffer advanceBuffer = stack.mallocInt(1);
			IntBuffer leftSideBearingBuffer = stack.mallocInt(1);
			
			for (int code = FIRST_CHAR; code < FIRST_CHAR + CHAR_COUNT; code++) {
				if (code == DEL_CHAR) {
					continue;
				}
				char character = (char)code;
				
				stbtt_GetCodepointHMetrics(fontInfo, code, advanceBuffer, leftSideBearingBuffer);
				float advance = advanceBuffer.get(0) * scale;
				
				ByteBuffer distanceField = stbtt_GetCodepointSDF(fontInfo, scale, code, GLYPH_PADDING,
					(byte)Texture.SIGNED_DISTANCE_FIELD_EDGE, distanceScale,
					widthBuffer, heightBuffer, offsetXBuffer, offsetYBuffer);
				
				// Whitespace characters
				if (distanceField == null) {
					glyphs.put(character, new Glyph(0, 0, 0, 0, 0, 0, 0, advance));
					continue;
				}
				
				try {
					int glyphWidth = widthBuffer.get(0);
					int glyphHeight = heightBuffer.get(0);
					
					Vector2i position = packer.pack(glyphWidth, glyphHeight);
					if (position == null) {
						throw new IOException("Font atlas is too small");
					}
					
					for (int row = 0; row < glyphHeight; row++) {
						for (int column = 0; column < glyphWidth; column++) {
							int destination = (position.y + row) * ATLAS_SIZE + position.x + column;
							alphaMask.put(destination, distanceField.get(row * glyphWidth + column));
						}
					}
					
					float regionY = ATLAS_SIZE - (position.y + glyphHeight);
					float x = offsetXBuffer.get(0);
					float y = offsetYBuffer.get(0) + glyphHeight;
					
					glyphs.put(character, new Glyph(glyphWidth, glyphHeight, position.x, regionY, x, y, glyphHeight, advance));
				} finally {
					stbtt_FreeSDF(distanceField);
				}
			}
		}
		
		return new Texture(null, ATLAS_SIZE, ATLAS_SIZE,
			createPixelData(ATLAS_SIZE, ATLAS_SIZE, alphaMask),
			Texture.Wrap.CLAMP_TO_EDGE, Texture.Filter.LINEAR,
			Texture.Encoding.SIGNED_DISTANCE_FIELD);
	}
	
	/**
	 * Calculates the scale factor for drawing this font at a given size.
	 * @param fontSize The font size, in pixels
	 */
	public float getScale(float fontSize) {
		return fontSize / BASE_SIZE;
	}
	
	public Vector2f getSize(CharSequence text) {
		return getSize(text, Font.DEFAULT_SIZE);
	}
	
	public Vector2f getSize(CharSequence text, float fontSize) {
		return new Vector2f(getWidth(text, fontSize), getHeight(text, fontSize));
	}
	
	public float getWidth(CharSequence text) {
		return getWidth(text, Font.DEFAULT_SIZE);
	}
	
	/**
	 * Calculates the width of a character sequence in this font.
	 * @param text The character sequence
	 * @param fontSize The font size, in pixels
	 * @return The width of the character sequence.
	 */
	public float getWidth(CharSequence text, float fontSize) {
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
		return Math.max(width, lineWidth) * getScale(fontSize);
	}
	
	public float getHeight(CharSequence text) {
		return getHeight(text, Font.DEFAULT_SIZE);
	}
	
	/**
	 * Calculates the height of a character sequence in this font.
	 * @param text The character sequence
	 * @param fontSize The font size, in pixels
	 * @return The height of the character sequence.
	 */
	public float getHeight(CharSequence text, float fontSize) {
		int lineCount = 1;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\n') {
				lineCount++;
			}
		}
		return lineCount * lineHeight * getScale(fontSize);
	}
	
	/**
	 * Draws text on the screen at the given coordinates using this font.
	 * @param renderer The renderer to use
	 * @param text The text to draw
	 * @param x The x position
	 * @param y The y position
	 * @param z The z position
	 * @param c The color to draw the text in
	 * @param fontSize The font size, in pixels
	 */
	public void drawText(Renderer renderer, CharSequence text, float x, float y, float z, Color c, float fontSize) {
		float scale = getScale(fontSize);
		float lineHeight = this.lineHeight * scale;
		float drawX = x;
		float drawY = y;
		
		float textHeight = getHeight(text, fontSize);
		if (textHeight > lineHeight) {
			drawY += textHeight - lineHeight;
		}
		
		drawY -= descent * scale;
		
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '\n') {
				drawY -= lineHeight;
				drawX = x;
				continue;
			}
			if (character == '\r') {
				continue;
			}
			Glyph glyph = glyphs.get(character);
			if (glyph == null) {
				continue;
			}
			
			// Only draw non-whitespace glyphs
			if (glyph.regionWidth > 0) {
				float glyphX = Math.round(drawX);
				float glyphY = Math.round(drawY);
				
				renderer.drawQuad(texture,
					glyphX + glyph.x * scale, glyphY - glyph.y * scale, z,
					glyph.regionX, glyph.regionY, glyph.regionWidth, glyph.regionHeight,
					glyph.regionWidth * scale, glyph.regionHeight * scale, c);
			}
			
			drawX += glyph.advance * scale;
		}
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