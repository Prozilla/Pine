package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Circle;

public class CircleRenderer extends MeshRenderer<Circle> {
	
	public CircleRenderer(Circle circle) {
		super(circle);
	}
	
	public CircleRenderer(Circle circle, Material<?> material) {
		super(circle, material);
	}
	
}
