package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Circle;

public class CircleRenderer extends MeshRenderer<Circle> {
	
	public CircleRenderer(Circle circle) {
		super(circle);
	}
	
	public CircleRenderer(Circle circle, Color color) {
		super(circle, color);
	}
	
	public CircleRenderer(Circle circle, TextureAsset texture) {
		super(circle, texture);
	}
	
	public CircleRenderer(Circle circle, TextureAsset texture, Color color) {
		super(circle, texture, color);
	}
	
}
