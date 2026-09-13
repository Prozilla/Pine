package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.CircleRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.mesh.Circle;

public class CirclePrefab extends MeshPrefab<Circle> {
	
	public CirclePrefab(Circle circle) {
		super(circle);
	}
	
	public CirclePrefab(Circle circle, Color color) {
		super(circle, color);
	}
	
	public CirclePrefab(Circle circle, String texturePath) {
		super(circle, texturePath);
	}
	
	public CirclePrefab(Circle circle, TextureAsset texture) {
		super(circle, texture);
	}
	
	public CirclePrefab(Circle circle, String texturePath, Color color) {
		super(circle, texturePath, color);
	}
	
	public CirclePrefab(Circle circle, TextureAsset texture, Color color) {
		super(circle, texture, color);
	}
	
	@Override
	protected MeshRenderer<Circle> createRenderer(Circle circle, TextureAsset texture, Color color) {
		return new CircleRenderer(circle, texture, color);
	}
	
}
