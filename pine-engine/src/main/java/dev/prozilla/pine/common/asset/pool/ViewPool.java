package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.core.entity.prefab.ui.HTMLParser;
import dev.prozilla.pine.core.entity.prefab.ui.View;

public final class ViewPool extends TextAssetPool<View> implements MultiAssetLoader<View> {
	
	private static final HTMLParser parser = new HTMLParser();
	
	@Override
	public View load(String path) {
		return super.load(path);
	}
	
	@Override
	protected View parse(String path, String content) {
		if (!parser.parse(content)) {
			return fail(content, "Error while parsing: " + parser.getError(), null);
		}
		
		return new View(parser.getResult(), parser.getTitle(), path);
	}
	
}
