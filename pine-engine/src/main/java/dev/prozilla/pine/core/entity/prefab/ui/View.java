package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.ui.ViewNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.Scene;

public class View implements Asset {
	
	public NodePrefab prefab;
	public String name;
	public String path;
	public Controller controller;
	
	public View(NodePrefab prefab) {
		this(prefab, null);
	}
	
	public View(NodePrefab prefab, String name) {
		this(prefab, name, null);
	}
	
	public View(NodePrefab prefab, String name, String path) {
		this.prefab = prefab;
		this.name = name;
		this.path = path;
	}
	
	/**
	 * Adds this view to a parent entity.
	 * @param parent The parent entity
	 * @return The new instance of this view.
	 */
	public Entity instantiate(Entity parent) {
		return load(parent.addChild(prefab));
	}
	
	/**
	 * Adds this view to a scene.
	 * @return The new instance of this view.
	 */
	public Entity instantiate(Scene scene) {
		return load(scene.addEntity(prefab));
	}
	
	private Entity load(Entity entity) {
		entity.addComponent(new ViewNode(this, controller));
		return entity;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void destroy() {
		AssetPools.views.remove(this);
	}
	
}
