package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class CuboidRenderer extends MeshRenderer<Cuboid> {
	
	public CuboidRenderer(Cuboid cuboid) {
		super(cuboid);
	}
	
	public CuboidRenderer(Cuboid cuboid, Color color) {
		super(cuboid, color);
	}
	
	public CuboidRenderer(Cuboid cuboid, TextureAsset texture) {
		super(cuboid, texture);
	}
	
	public CuboidRenderer(Cuboid cuboid, TextureAsset texture, Color color) {
		super(cuboid, texture, color);
	}
	
}
