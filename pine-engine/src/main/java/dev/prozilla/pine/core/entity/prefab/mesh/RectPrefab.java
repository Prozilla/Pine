package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.component.mesh.RectRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Rect;

public class RectPrefab extends MeshPrefab<Rect> {
	
	public RectPrefab(Rect rect) {
		super(rect);
	}
	
	public RectPrefab(Rect rect, Material<?> material) {
		super(rect, material);
	}
	
	@Override
	protected MeshRenderer<Rect> createRenderer(Rect rect, Material<?> material) {
		return new RectRenderer(rect, material);
	}
	
}
