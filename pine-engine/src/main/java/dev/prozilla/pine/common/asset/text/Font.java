package dev.prozilla.pine.common.asset.text;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
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
 * Rewritten to use STB TrueType (no AWT dependencies).
 */
public class Font implements Asset {
	
	/** The glyphs for each char. */
	private final Map<Character, Glyph> glyphs;
	private final Texture texture;
	
	/** Height of the font. */
	private int fontHeight;
	private final int size;
	public String path;
	
	public static final int DEFAULT_SIZE = 16;
	public static final Color DEFAULT_COLOR = Color.white();
	
	private static final int FIRST_CHAR = 32;
	private static final int CHAR_COUNT = 224; // 256 - 32
	
	/**
	 * Creates a font from a TTF input stream.
	 */
	public Font(InputStream in, int size, boolean antiAlias) throws IOException {
		glyphs = new HashMap<>();
		this.size = size;
		this.texture = createFontTexture(in, size);
	}
	
	public Font(InputStream in, int size) throws IOException {
		this(in, size, true);
	}
	
	public Font(int size) {
		try (InputStream defaultFont = Font.class.getResourceAsStream("/fonts/Inconsolata.ttf")) {
			if (defaultFont == null) throw new RuntimeException("Default font missing.");
			glyphs = new HashMap<>();
			this.size = size;
			this.texture = createFontTexture(defaultFont, size);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Texture createFontTexture(InputStream fontStream, int fontSize) throws IOException {
		ByteBuffer fontBuffer = null;
		try {
			// Load font bytes
			byte[] fontBytes = fontStream.readAllBytes();
			fontBuffer = memAlloc(fontBytes.length);
			fontBuffer.put(fontBytes).flip();
			
			int texWidth = 512;
			int texHeight = 512;
			ByteBuffer bitmap = BufferUtils.createByteBuffer(texWidth * texHeight);
			
			// Bake ASCII glyphs into a bitmap
			STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(CHAR_COUNT);
			stbtt_BakeFontBitmap(fontBuffer, fontSize, bitmap, texWidth, texHeight, FIRST_CHAR, cdata);
			
			// Convert grayscale to RGBA (white text + alpha) and flip vertically
			ByteBuffer rgba = BufferUtils.createByteBuffer(texWidth * texHeight * 4);
			for (int y = 0; y < texHeight; y++) {
				for (int x = 0; x < texWidth; x++) {
					int srcIndex = y * texWidth + x;
					int dstIndex = (texHeight - 1 - y) * texWidth + x;
					
					byte alpha = bitmap.get(srcIndex);
					
					rgba.put(dstIndex * 4, (byte) 0xFF);
					rgba.put(dstIndex * 4 + 1, (byte) 0xFF);
					rgba.put(dstIndex * 4 + 2, (byte) 0xFF);
					rgba.put(dstIndex * 4 + 3, alpha);
				}
			}
			rgba.flip();
			
			Texture tex = new Texture(null, texWidth, texHeight, rgba);
			
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
				fontHeight = (int)((ascent.get(0) - descent.get(0) + lineGap.get(0)) * scale);
				
				// Measure each character’s quad size
				FloatBuffer xpos = stack.floats(0.0f);
				FloatBuffer ypos = stack.floats(0.0f);
				STBTTAlignedQuad quad = STBTTAlignedQuad.malloc(stack);
				
				for (int i = 0; i < CHAR_COUNT; i++) {
					int code = FIRST_CHAR + i;
					
					xpos.put(0, 0.0f);
					ypos.put(0, 0.0f);
					
					stbtt_GetBakedQuad(cdata, texWidth, texHeight, i, xpos, ypos, quad, true);
					
					int x = (int)(quad.s0() * texWidth);
					int y = (int)((1f - quad.t1()) * texHeight);
					int width  = (int)((quad.s1() - quad.s0()) * texWidth);
					int height = (int)((quad.t1() - quad.t0()) * texHeight);
					float advance = quad.x1() - quad.x0();
					
					Glyph g = new Glyph(width, height, x, y, advance);
					glyphs.put((char) code, g);
				}
			}
			
			cdata.free();
			fontInfo.free();
			return tex;
		} finally {
			if (fontBuffer != null) memFree(fontBuffer);
		}
	}
	
	public int getWidth(CharSequence text) {
		int width = 0;
		int lineWidth = 0;
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '\n') {
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				continue;
			}
			if (character == '\r') continue;
			Glyph glyph = glyphs.get(character);
			if (glyph != null) lineWidth += glyph.width;
		}
		return Math.max(width, lineWidth);
	}
	
	public int getHeight(CharSequence text) {
		int height = 0;
		int lineHeight = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				height += lineHeight;
				lineHeight = 0;
				continue;
			}
			if (c == '\r') continue;
			Glyph glyph = glyphs.get(c);
			if (glyph != null) lineHeight = Math.max(lineHeight, glyph.height);
		}
		height += lineHeight;
		return height;
	}
	
	public int getSize() {
		return size;
	}
	
	public void drawText(Renderer renderer, CharSequence text, float x, float y, float z, Color c) {
		float drawX = x;
		float drawY = y;
		
		int textHeight = getHeight(text);
		if (textHeight > fontHeight) {
			drawY += textHeight - fontHeight;
		}
		
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\n') {
				drawY -= fontHeight;
				drawX = x;
				continue;
			}
			if (ch == '\r') continue;
			Glyph glyph = glyphs.get(ch);
			if (glyph != null) {
				renderer.drawTextureRegion(texture, drawX, drawY, z, glyph.x, glyph.y, glyph.width, glyph.height, c);
				drawX += glyph.advance;
			}
		}
	}
	
	public void drawText(Renderer renderer, CharSequence text, float x, float y, float z) {
		drawText(renderer, text, x, y, z, DEFAULT_COLOR);
	}
	
	public Font setSize(int size) {
		return AssetPools.fonts.load(path, size);
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
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
	
}