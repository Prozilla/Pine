package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Mesh;
import dev.prozilla.pine.core.rendering.mesh.Meshes;

public class MeshesRenderer<A extends Mesh, B extends Mesh> extends MeshRenderer<Meshes<A, B>> {
	
	public MeshesRenderer(Meshes<A, B> meshes) {
		super(meshes);
	}
	
	public MeshesRenderer(Meshes<A, B> meshes, Material<?> material) {
		super(meshes, material);
	}
	
}
