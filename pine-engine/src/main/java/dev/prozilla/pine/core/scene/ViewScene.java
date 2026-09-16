package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.View;

public class ViewScene extends Scene {
	
	protected View view;
	protected NodeRoot nodeRoot;
	protected Node viewNode;
	protected StyleSheet styleSheet;
	
	public ViewScene(String viewPath, String styleSheetPath) {
		this(viewPath);
		setStyleSheet(styleSheetPath);
	}
	
	public ViewScene(String viewPath) {
		this(AssetPools.views.load(viewPath));
	}
	
	public ViewScene(View view, StyleSheet styleSheet) {
		this(view);
		setStyleSheet(styleSheet);
	}
	
	public ViewScene(View view) {
		super(view.name);
		this.view = view;
	}
	
	@Override
	protected void load(Prefab cameraPrefab) throws IllegalStateException {
		super.load(cameraPrefab);
		
		if (view.name != null) {
			getConfig().window.title.setValue(view.name);
		}
		if (styleSheet != null) {
			view.prefab.setStyleSheet(styleSheet);
		}
		
		NodeRootPrefab rootPrefab = new NodeRootPrefab();
		rootPrefab.addChild(view.prefab);
		
		Entity root = addEntity(rootPrefab);
		nodeRoot = root.getComponent(NodeRoot.class);
	}
	
	public void setStyleSheet(String path) {
		setStyleSheet(AssetPools.styleSheets.load(path));
	}
	
	public void setStyleSheet(StyleSheet styleSheet) {
		this.styleSheet = styleSheet;
	}
	
	public NodeRoot getRoot() {
		return nodeRoot;
	}
	
	public Node getView() {
		if (viewNode != null || nodeRoot.getFirstChild() == null) {
			return viewNode;
		}
		
		viewNode = nodeRoot.getFirstChild().getComponent(Node.class);
		return viewNode;
	}
	
}
