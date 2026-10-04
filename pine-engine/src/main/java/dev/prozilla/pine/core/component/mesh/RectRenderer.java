package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Rect;

public class RectRenderer extends MeshRenderer<Rect> {
	
	public RectRenderer(Rect rect) {
		super(rect);
	}
	
	public RectRenderer(Rect rect, Material<?> material) {
		super(rect, material);
	}
	
}
