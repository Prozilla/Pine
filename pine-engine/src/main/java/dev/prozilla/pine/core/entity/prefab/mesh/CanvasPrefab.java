package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.CanvasRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Canvas;

public class CanvasPrefab extends MeshPrefab<Canvas> {
	
	public CanvasPrefab(Canvas canvas) {
		super(canvas);
	}
	
	public CanvasPrefab(Canvas canvas, Material<?> material) {
		super(canvas, material);
	}
	
	@Override
	protected MeshRenderer<Canvas> createRenderer(Canvas canvas, Material<?> material) {
		return new CanvasRenderer(canvas, material);
	}
	
}
