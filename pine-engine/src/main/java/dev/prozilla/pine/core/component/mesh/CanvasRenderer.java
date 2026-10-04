package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Canvas;

public class CanvasRenderer extends MeshRenderer<Canvas> {
	
	public CanvasRenderer(Canvas canvas) {
		super(canvas);
	}
	
	public CanvasRenderer(Canvas canvas, Material<?> material) {
		super(canvas, material);
	}
	
}
