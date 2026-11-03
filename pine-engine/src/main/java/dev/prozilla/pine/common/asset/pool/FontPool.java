package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.PathUtils;

import java.io.IOException;
import java.io.InputStream;

public final class FontPool extends AssetPool<Font> implements MultiAssetLoader<Font> {
	
	@Override
	public Font load(String path) {
		return super.load(path);
	}
	
	@Override
	protected Font createAsset(String path) {
		Font font;
		try (InputStream stream = FontPool.class.getResourceAsStream(path)) {
			if (stream == null) {
				return fail(path, NOT_FOUND_ERROR);
			}
			
			font = new Font(stream);
			font.path = path;
		} catch (IOException e) {
			return fail(path, e);
		}
		
		return font;
	}
	
	@Override
	protected String normalize(String path) {
		return PathUtils.addLeadingSlash(path);
	}
	
}