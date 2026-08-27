package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.CanvasRenderer;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.mesh.Canvas;

public class CanvasPrefab extends MeshPrefab<Canvas> {
	
	public CanvasPrefab(Canvas canvas) {
		super(canvas);
	}
	
	public CanvasPrefab(Canvas canvas, Color color) {
		super(canvas, color);
	}
	
	public CanvasPrefab(Canvas canvas, String texturePath) {
		super(canvas, texturePath);
	}
	
	public CanvasPrefab(Canvas canvas, TextureAsset texture) {
		super(canvas, texture);
	}
	
	public CanvasPrefab(Canvas canvas, String texturePath, Color color) {
		super(canvas, texturePath, color);
	}
	
	public CanvasPrefab(Canvas canvas, TextureAsset texture, Color color) {
		super(canvas, texture, color);
	}
	
	@Override
	protected MeshRenderer<Canvas> createRenderer(Canvas canvas, TextureAsset texture, Color color) {
		return new CanvasRenderer(canvas, texture, color);
	}
	
}
