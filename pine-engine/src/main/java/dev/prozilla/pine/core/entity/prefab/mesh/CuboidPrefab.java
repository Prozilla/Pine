package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.CuboidRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class CuboidPrefab extends MeshPrefab<Cuboid> {
	
	public CuboidPrefab(Cuboid cuboid) {
		super(cuboid);
	}
	
	public CuboidPrefab(Cuboid cuboid, Material<?> material) {
		super(cuboid, material);
	}
	
	@Override
	protected MeshRenderer<Cuboid> createRenderer(Cuboid cuboid, Material<?> material) {
		return new CuboidRenderer(cuboid, material);
	}
	
}
