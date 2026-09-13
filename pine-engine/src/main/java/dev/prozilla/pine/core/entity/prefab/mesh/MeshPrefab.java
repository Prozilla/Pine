package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

public class MeshPrefab<M extends Mesh> extends Prefab {
	
	protected M mesh;
	protected TextureAsset texture;
	protected Color color;
	
	public MeshPrefab(M mesh) {
		this(mesh, (TextureAsset)null);
	}
	
	public MeshPrefab(M mesh, Color color) {
		this(mesh, (TextureAsset)null, color);
	}
	
	public MeshPrefab(M mesh, String texturePath) {
		this(mesh, AssetPools.textures.load(texturePath), null);
	}
	
	public MeshPrefab(M mesh, TextureAsset texture) {
		this(mesh, texture, null);
	}
	
	public MeshPrefab(M mesh, String texturePath, Color color) {
		this(mesh, AssetPools.textures.load(texturePath), color);
	}
	
	public MeshPrefab(M mesh, TextureAsset texture, Color color) {
		setName("Mesh");
		
		this.mesh = mesh;
		this.texture = texture;
		this.color = color;
	}
	
	public void setMesh(M mesh) {
		this.mesh = mesh;
	}
	
	public void setTexture(TextureAsset texture) {
		this.texture = texture;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	protected MeshRenderer<M> createRenderer(M mesh, TextureAsset texture, Color color) {
		return new MeshRenderer<>(mesh, texture, color);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(createRenderer(mesh, texture, Cloneable.cloneOf(color)));
	}
	
}
