package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Line;

public class LineRenderer extends MeshRenderer<Line> {
	
	public LineRenderer(Line line) {
		super(line);
	}
	
	public LineRenderer(Line line, Color color) {
		super(line, color);
	}
	
	public LineRenderer(Line line, TextureAsset texture) {
		super(line, texture);
	}
	
	public LineRenderer(Line line, TextureAsset texture, Color color) {
		super(line, texture, color);
	}
	
}
