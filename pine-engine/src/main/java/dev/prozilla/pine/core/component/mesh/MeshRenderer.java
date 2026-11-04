package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.Holdable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

public class MeshRenderer<M extends Mesh> extends Component {
	
	public M mesh;
	protected Material<?> material;
	
	public MeshRenderer(M mesh) {
		this(mesh, null);
	}
	
	public MeshRenderer(M mesh, Material<?> material) {
		this.mesh = mesh;
		this.material = Holdable.hold(material);
	}
	
	public M getMesh() {
		return mesh;
	}
	
	/**
	 * Holds the texture of the material of this renderer in the scene to avoid reloading it.
	 */
	public void preserveTexture() {
		getScene().addAsset(getTexture());
	}
	
	public TextureAsset getTexture() {
		return material != null ? material.getTexture() : null;
	}
	
	public Color getColor() {
		return material != null ? material.color : null;
	}
	
	public Material<?> getMaterial() {
		return material;
	}
	
	public void setMaterial(Material<?> material) {
		this.material = Holdable.swap(this.material, material);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		mesh = Destructible.destroy(mesh);
		material = Holdable.drop(material);
	}
	
}
