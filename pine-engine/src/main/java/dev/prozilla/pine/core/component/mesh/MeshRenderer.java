package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

public class MeshRenderer<M extends Mesh> extends Component {
	
	public M mesh;
	public Material<?> material;
	
	public MeshRenderer(M mesh) {
		this(mesh, null);
	}
	
	public MeshRenderer(M mesh, Material<?> material) {
		this.mesh = mesh;
		this.material = material;
	}
	
	public M getMesh() {
		return mesh;
	}
	
	public TextureAsset getTexture() {
		return material != null ? material.texture : null;
	}
	
	public Color getColor() {
		return material != null ? material.color : null;
	}
	
}
