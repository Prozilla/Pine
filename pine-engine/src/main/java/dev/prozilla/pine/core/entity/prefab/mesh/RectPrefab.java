package dev.prozilla.pine.core.entity.prefab.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.component.mesh.RectRenderer;
import dev.prozilla.pine.core.rendering.mesh.Rect;

public class RectPrefab extends MeshPrefab<Rect> {
	
	public RectPrefab(Rect rect) {
		super(rect);
	}
	
	public RectPrefab(Rect rect, Color color) {
		super(rect, color);
	}
	
	public RectPrefab(Rect rect, String texturePath) {
		super(rect, texturePath);
	}
	
	public RectPrefab(Rect rect, TextureAsset texture) {
		super(rect, texture);
	}
	
	public RectPrefab(Rect rect, String texturePath, Color color) {
		super(rect, texturePath, color);
	}
	
	public RectPrefab(Rect rect, TextureAsset texture, Color color) {
		super(rect, texture, color);
	}
	
	@Override
	protected MeshRenderer<Rect> createRenderer(Rect rect, TextureAsset texture, Color color) {
		return new RectRenderer(rect, texture, color);
	}
	
}
