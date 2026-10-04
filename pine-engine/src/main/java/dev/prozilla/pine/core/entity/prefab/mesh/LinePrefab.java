package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.core.component.mesh.LineRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.Line;

public class LinePrefab extends MeshPrefab<Line> {
	
	public LinePrefab(Line line) {
		super(line);
	}
	
	public LinePrefab(Line line, Material<?> material) {
		super(line, material);
	}
	
	@Override
	protected MeshRenderer<Line> createRenderer(Line line, Material<?> material) {
		return new LineRenderer(line, material);
	}
	
}
