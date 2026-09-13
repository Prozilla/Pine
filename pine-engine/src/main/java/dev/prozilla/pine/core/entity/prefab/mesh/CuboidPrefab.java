package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.CuboidRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class CuboidPrefab extends MeshPrefab<Cuboid> {
	
	public CuboidPrefab(Cuboid cuboid) {
		super(cuboid);
	}
	
	public CuboidPrefab(Cuboid cuboid, Color color) {
		super(cuboid, color);
	}
	
	public CuboidPrefab(Cuboid cuboid, String texturePath) {
		super(cuboid, texturePath);
	}
	
	public CuboidPrefab(Cuboid cuboid, TextureAsset texture) {
		super(cuboid, texture);
	}
	
	public CuboidPrefab(Cuboid cuboid, String texturePath, Color color) {
		super(cuboid, texturePath, color);
	}
	
	public CuboidPrefab(Cuboid cuboid, TextureAsset texture, Color color) {
		super(cuboid, texture, color);
	}
	
	@Override
	protected MeshRenderer<Cuboid> createRenderer(Cuboid cuboid, TextureAsset texture, Color color) {
		return new CuboidRenderer(cuboid, texture, color);
	}
	
}
