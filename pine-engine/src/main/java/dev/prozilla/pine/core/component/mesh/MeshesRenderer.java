package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Mesh;
import dev.prozilla.pine.core.rendering.mesh.Meshes;

public class MeshesRenderer<A extends Mesh, B extends Mesh> extends MeshRenderer<Meshes<A, B>> {
	
	public MeshesRenderer(Meshes<A, B> meshes) {
		super(meshes);
	}
	
	public MeshesRenderer(Meshes<A, B> meshes, Color color) {
		super(meshes, color);
	}
	
	public MeshesRenderer(Meshes<A, B> meshes, TextureAsset texture) {
		super(meshes, texture);
	}
	
	public MeshesRenderer(Meshes<A, B> meshes, TextureAsset texture, Color color) {
		super(meshes, texture, color);
	}
	
}
