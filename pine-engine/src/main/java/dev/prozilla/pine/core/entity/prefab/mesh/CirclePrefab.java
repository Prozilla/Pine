package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.CircleRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Circle;

public class CirclePrefab extends MeshPrefab<Circle> {
	
	public CirclePrefab(Circle circle) {
		super(circle);
	}
	
	public CirclePrefab(Circle circle, Material<?> material) {
		super(circle, material);
	}
	
	@Override
	protected MeshRenderer<Circle> createRenderer(Circle circle, Material<?> material) {
		return new CircleRenderer(circle, material);
	}
	
}
