package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

public class MeshRenderer<M extends Mesh> extends Component {
	
	public M mesh;
	public TextureAsset texture;
	public Color color;
	
	public MeshRenderer(M mesh) {
		this(mesh, null, null);
	}
	
	public MeshRenderer(M mesh, Color color) {
		this(mesh, null, color);
	}
	
	public MeshRenderer(M mesh, TextureAsset texture) {
		this(mesh, texture, null);
	}
	
	public MeshRenderer(M mesh, TextureAsset texture, Color color) {
		this.mesh = mesh;
		this.texture = texture;
		this.color = color;
	}
	
	public M getMesh() {
		return mesh;
	}
	
}
