package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.component.mesh.MeshesRenderer;
import dev.prozilla.pine.core.rendering.mesh.Mesh;
import dev.prozilla.pine.core.rendering.mesh.Meshes;

public class MeshesPrefab<A extends Mesh, B extends Mesh> extends MeshPrefab<Meshes<A, B>> {
	
	public MeshesPrefab(Meshes<A, B> meshes) {
		super(meshes);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, Color color) {
		super(meshes, color);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, String texturePath) {
		super(meshes, texturePath);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, TextureAsset texture) {
		super(meshes, texture);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, String texturePath, Color color) {
		super(meshes, texturePath, color);
	}
	
	public MeshesPrefab(Meshes<A, B> meshes, TextureAsset texture, Color color) {
		super(meshes, texture, color);
	}
	
	@Override
	protected MeshRenderer<Meshes<A, B>> createRenderer(Meshes<A, B> meshes, TextureAsset texture, Color color) {
		return new MeshesRenderer<>(meshes, texture, color);
	}
	
}
