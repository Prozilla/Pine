package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.property.style.CSSParser;
import dev.prozilla.pine.common.property.style.StyleSheet;

public final class StyleSheetPool extends TextAssetPool<StyleSheet> implements MultiAssetLoader<StyleSheet> {
	
	@Override
	public StyleSheet load(String path) {
		return super.load(path);
	}
	
	@Override
	protected StyleSheet parse(String path, String content) {
		StyleSheet styleSheet;
		
		CSSParser parser = new CSSParser();
		if (!parser.parse(content)) {
			return fail(content, "Error while parsing: " + parser.getError(), null);
		} else {
			styleSheet = parser.getResult();
		}
		
		return styleSheet;
	}
	
}
