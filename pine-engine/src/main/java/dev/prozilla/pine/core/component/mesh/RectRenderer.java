package dev.prozilla.pine.core.component.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.mesh.Rect;

public class RectRenderer extends MeshRenderer<Rect> {
	
	public RectRenderer(Rect rect) {
		super(rect);
	}
	
	public RectRenderer(Rect rect, Color color) {
		super(rect, color);
	}
	
	public RectRenderer(Rect rect, TextureAsset texture) {
		super(rect, texture);
	}
	
	public RectRenderer(Rect rect, TextureAsset texture, Color color) {
		super(rect, texture, color);
	}
	
}
