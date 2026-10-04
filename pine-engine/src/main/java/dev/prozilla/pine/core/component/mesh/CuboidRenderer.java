package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class CuboidRenderer extends MeshRenderer<Cuboid> {
	
	public CuboidRenderer(Cuboid cuboid) {
		super(cuboid);
	}
	
	public CuboidRenderer(Cuboid cuboid, Material<?> material) {
		super(cuboid, material);
	}
	
}
