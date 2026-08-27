package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.LineRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.mesh.Line;

public class LinePrefab extends MeshPrefab<Line> {
	
	public LinePrefab(Line line) {
		super(line);
	}
	
	public LinePrefab(Line line, Color color) {
		super(line, color);
	}
	
	public LinePrefab(Line line, String texturePath) {
		super(line, texturePath);
	}
	
	public LinePrefab(Line line, TextureAsset texture) {
		super(line, texture);
	}
	
	public LinePrefab(Line line, String texturePath, Color color) {
		super(line, texturePath, color);
	}
	
	public LinePrefab(Line line, TextureAsset texture, Color color) {
		super(line, texture, color);
	}
	
	@Override
	protected MeshRenderer<Line> createRenderer(Line line, TextureAsset texture, Color color) {
		return new LineRenderer(line, texture, color);
	}
	
}
