package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Line;

public class LineRenderer extends MeshRenderer<Line> {
	
	public LineRenderer(Line line) {
		super(line);
	}
	
	public LineRenderer(Line line, Material<?> material) {
		super(line, material);
	}
	
}
