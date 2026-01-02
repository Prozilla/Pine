package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.property.style.CSSParser;
import dev.prozilla.pine.common.property.style.HotStyleSheet;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.util.checks.Checks;

import java.io.InputStream;

public final class StyleSheetPool extends TextAssetPool<StyleSheet> implements MultiAssetLoader<StyleSheet> {
	
	private StyleSheet styleSheet;
	
	private static final CSSParser parser = new CSSParser();
	
	/**
	 * Reloads a stylesheet.
	 * @param styleSheet The stylesheet to reload
	 * @return The reloaded stylesheet.
	 */
	public StyleSheet reload(StyleSheet styleSheet) {
		return reload(styleSheet.getPath(), styleSheet);
	}
	
	/**
	 * Reloads a stylesheet in this pool, or loads a new stylesheet if it is not in this pool yet.
	 * @param path The path of the stylesheet
	 * @return The reloaded stylesheet.
	 */
	public StyleSheet reload(String path) {
		return reload(path, pool.get(pathToKey(path)));
	}
	
	private StyleSheet reload(String path, StyleSheet styleSheet) {
		Checks.isNotNull(path, "path");
		
		if (styleSheet != null) {
			styleSheet.reset();
			this.styleSheet = styleSheet;
		}
		
		path = normalize(path);
		String key = createKey(path);
		pool.remove(key);
		
		return load(path);
	}
	
	/**
	 * Loads a stylesheet with/without hot reloading.
	 * @param path The path of the stylesheet
	 * @param hot Whether to enable/disable hot reloading
	 * @return The stylesheet
	 */
	public StyleSheet load(String path, boolean hot) {
		return hot ? hotLoad(path) : load(path);
	}
	
	/**
	 * Loads a stylesheet with hot reloading.
	 * @param path The path of the stylesheet
	 * @return The stylesheet with hot reloading.
	 */
	public HotStyleSheet hotLoad(String path) {
		Checks.isNotNull(path, "path");
		path = normalize(path);
		
		// Create hot stylesheet
		DirectoryWatcher directoryWatcher = AssetPools.directoryWatchers.load(PathUtils.getParent(path));
		HotStyleSheet styleSheet = load(path).toHotStyleSheet(directoryWatcher);
		
		// Override normal stylesheet with hot stylesheet
		String key = createKey(path);
		pool.put(key, styleSheet);
		
		return styleSheet;
	}
	
	@Override
	public StyleSheet load(String path) {
		return super.load(path);
	}
	
	@Override
	protected InputStream createInputStream(String path) {
		if (styleSheet == null) {
			return super.createInputStream(path);
		} else {
			return styleSheet.createInputStream();
		}
	}
	
	@Override
	protected StyleSheet parse(String path, String content) {
		if (!parser.parse(content, styleSheet)) {
			return fail(content, "Error while parsing: " + parser.getError(), null);
		} else {
			styleSheet = parser.getResult();
		}
		
		styleSheet.path = path;
		return styleSheet;
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		styleSheet = null;
	}
	
}
