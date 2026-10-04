package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.component.mesh.MeshesRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Mesh;
import dev.prozilla.pine.core.rendering.mesh.Meshes;

public class MeshesPrefab<A extends Mesh, B extends Mesh> extends MeshPrefab<Meshes<A, B>> {
	
	public MeshesPrefab(Meshes<A, B> meshes) {
		super(meshes);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, Material<?> material) {
		super(meshes, material);
	}
	
	@Override
	protected MeshRenderer<Meshes<A, B>> createRenderer(Meshes<A, B> meshes, Material<?> material) {
		return new MeshesRenderer<>(meshes, material);
	}
	
}
