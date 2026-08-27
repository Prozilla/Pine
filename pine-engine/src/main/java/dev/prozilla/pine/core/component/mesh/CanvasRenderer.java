package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Canvas;

public class CanvasRenderer extends MeshRenderer<Canvas> {
	
	public CanvasRenderer(Canvas canvas) {
		super(canvas);
	}
	
	public CanvasRenderer(Canvas canvas, Color color) {
		super(canvas, color);
	}
	
	public CanvasRenderer(Canvas canvas, TextureAsset texture) {
		super(canvas, texture);
	}
	
	public CanvasRenderer(Canvas canvas, TextureAsset texture, Color color) {
		super(canvas, texture, color);
	}
	
}
