package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.view.Controller;
import dev.prozilla.pine.common.asset.view.HTMLParser;
import dev.prozilla.pine.common.asset.view.View;

import java.util.HashMap;
import java.util.Map;

public final class ViewPool extends TextAssetPool<View> implements MultiAssetLoader<View> {
	
	private final Map<String, Controller> controllers;
	
	private static final HTMLParser parser = new HTMLParser();
	
	public ViewPool() {
		controllers = new HashMap<>();
	}
	
	public void addController(String viewPath, Controller controller) {
		controllers.compute(pathToKey(viewPath), (key, value) -> value != null ? value.then(controller) : controller);
	}
	
	@Override
	public View load(String path) {
		return super.load(path);
	}
	
	@Override
	protected View parse(String path, String content) {
		if (!parser.parse(content)) {
			return fail(content, "Error while parsing: " + parser.getError());
		}
		
		View view = new View(parser.getResult(), parser.getTitle(), path);
		view.addController(controllers.get(path));
		view.addController(parser.getController());
		view.prefab.addStyleSheets(parser.getStyleSheets());
		
		return view;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		controllers.clear();
	}
	
}
