package dev.prozilla.pine.common.asset.model;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.selection.WrapMode;
import dev.prozilla.pine.core.entity.prefab.mesh.MeshPrefab;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;

public class Model implements Asset {
	
	public String path;
	public StaticMesh[] meshes;
	public Material<?>[] materials;
	
	public Model(StaticMesh... meshes) {
		this(meshes, null);
	}
	
	public Model(StaticMesh[] meshes, Material<?>[] materials) {
		this.meshes = meshes;
		this.materials = materials;
	}
	
	public StaticMesh getFirstMesh() {
		return getMesh(0);
	}
	
	public StaticMesh getLastMesh() {
		return getMesh(meshes.length - 1);
	}
	
	@SuppressWarnings("unchecked")
	public MeshPrefab<StaticMesh>[] createPrefabs() {
		MeshPrefab<StaticMesh>[] prefabs = new MeshPrefab[meshes.length];
		for (int i = 0; i < prefabs.length; i++) {
			prefabs[i] = createPrefab(i);
		}
		return prefabs;
	}
	
	public MeshPrefab<StaticMesh> createPrefab(int index) {
		return new MeshPrefab<>(getMesh(index), getMaterial(index));
	}
	
	public StaticMesh getMesh(int index) {
		return WrapMode.CLIP.getElement(index, meshes);
	}
	
	public Material<?> getMaterial(int index) {
		return WrapMode.CLIP.getElement(index, materials);
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
