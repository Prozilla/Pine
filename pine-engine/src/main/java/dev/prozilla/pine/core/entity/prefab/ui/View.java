package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;

public class View implements Asset {
	
	public NodePrefab prefab;
	public String name;
	public String path;
	
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
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void destroy() {
		AssetPools.views.remove(this);
	}
	
}
