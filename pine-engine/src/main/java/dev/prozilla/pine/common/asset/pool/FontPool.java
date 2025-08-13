package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.PathUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class FontPool extends AssetPool<Font> {
	
	private int size;
	
	public FontPool() {
		size = Font.DEFAULT_SIZE;
	}
	
	public Font load(String path, int size) {
		this.size = size;
		return load(path);
	}
	
	@Override
	public Font load(String path) {
		return super.load(path);
	}
	
	@Override
	protected Font createAsset(String path) {
		Font font;
		try (InputStream stream = AssetPools.class.getResourceAsStream(path)) {
			font = new Font(stream, size);
			font.path = path;
		} catch (FontFormatException | IOException e) {
			return fail(path, null, e);
		}
		
		return font;
	}
	
	@Override
	public boolean remove(Font asset) {
		size = asset.getSize();
		boolean removed = remove(asset.getPath());
		size = Font.DEFAULT_SIZE;
		return removed;
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		size = Font.DEFAULT_SIZE;
	}
	
	@Override
	protected String createKey(String path) {
		return Font.generateKey(path, size);
	}
	
	@Override
	protected String normalize(String path) {
		return PathUtils.addLeadingSlash(path);
	}
	
}
