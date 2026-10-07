package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.material.UnlitMaterial;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

public class MeshPrefab<M extends Mesh> extends Prefab {
	
	protected M mesh;
	protected Material<?> material;
	
	public MeshPrefab(M mesh) {
		this(mesh, null);
	}
	
	public MeshPrefab(M mesh, Material<?> material) {
		setName("Mesh");
		
		this.mesh = mesh;
		this.material = material;
	}
	
	public M getMesh() {
		return mesh;
	}
	
	public void setMesh(M mesh) {
		this.mesh = mesh;
	}
	
	public Material<?> getMaterial() {
		return material;
	}
	
	public void setMaterial(Material<?> material) {
		this.material = material;
	}
	
	public void setTexture(TextureAsset texture) {
		if (material == null) {
			material = new UnlitMaterial(texture);
		} else {
			material.texture = texture;
		}
	}
	
	public void setColor(Color color) {
		if (material == null) {
			material = new UnlitMaterial(color);
		} else {
			material.color = color;
		}
	}
	
	protected MeshRenderer<M> createRenderer(M mesh, Material<?> material) {
		return new MeshRenderer<>(mesh, material);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(createRenderer(mesh, material));
	}
	
}
