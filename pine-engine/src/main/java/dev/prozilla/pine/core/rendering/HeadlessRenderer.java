package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;

public class HeadlessRenderer extends Renderer {
	
	public HeadlessRenderer(Application application) {
		super(application);
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public void setRegion(int x, int y, int width, int height) {
	
	}
	
	@Override
	public void resetRegion() {
	
	}
	
	@Override
	public int getTextHeight(Font font, CharSequence text) {
		return 0;
	}
	
	@Override
	public int getTextWidth(Font font, CharSequence text) {
		return 0;
	}
	
	@Override
	public int getDebugTextHeight(CharSequence text) {
		return 0;
	}
	
	@Override
	public int getDebugTextWidth(CharSequence text) {
		return 0;
	}
	
	@Override
	public void resize() {
	
	}
	
	@Override
	public void flush() {
	
	}
	
	@Override
	public void clear() {
	
	}
	
	@Override
	public void drawText(Font font, CharSequence text, float x, float y, float z) {
	
	}
	
	@Override
	public void drawDebugText(CharSequence text, float x, float y) {
	
	}
	
	@Override
	public void drawTextureRegion(TextureBase texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float z, float s1, float t1, float s2, float t2, Color c) {
	
	}
	
}
