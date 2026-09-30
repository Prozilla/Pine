package dev.prozilla.pine.common.asset.model;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;

public class Model implements Asset {
	
	public String path;
	public StaticMesh[] meshes;
	
	public Model(StaticMesh... meshes) {
		this.meshes = meshes;
	}
	
	public StaticMesh getFirstMesh() {
		return getMesh(0);
	}
	
	public StaticMesh getLastMesh() {
		return getMesh(meshes.length - 1);
	}
	
	public StaticMesh getMesh(int index) {
		return index >= 0 && index < meshes.length ? meshes[index] : null;
	}
	
	public int getMeshCount() {
		return meshes.length;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void destroy() {
		AssetPools.models.remove(this);
	}
	
}
