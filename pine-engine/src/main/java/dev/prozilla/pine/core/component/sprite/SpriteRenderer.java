package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.rendering.mesh.Rect;
import dev.prozilla.pine.core.rendering.mesh.modifier.UVModifier;

// TODO: Remove
/**
 * A component for rendering 2D sprites in the world.
 * @deprecated
 */
public class SpriteRenderer extends MeshRenderer<Rect> {
	
	private final UVModifier uvModifier;
	
	// Transformations
	public boolean mirrorHorizontally;
	public boolean mirrorVertically;
	
	// Cropping
	public boolean cropToRegion;
	public Vector2f regionOffset;
	public Vector2f regionSize;
	
	private final Vector2f textureSize;
	
	public SpriteRenderer(TextureAsset texture) {
		this(texture, null);
	}
	
	public SpriteRenderer(TextureAsset texture, Color color) {
		super(new Rect(new Vector3f(), new Vector2f(texture.getWidth(), texture.getHeight())), texture, color);
		
		textureSize = new Vector2f(texture.getWidth(), texture.getHeight());
		
		mirrorHorizontally = false;
		mirrorVertically = false;
		
		cropToRegion = false;
		regionOffset = new Vector2f();
		regionSize = new Vector2f(texture.getWidth(), texture.getHeight());
		
		uvModifier = new UVModifier(0, 0, 1, 1);
		mesh.addModifier(uvModifier);
		updateMesh();
	}
	
	@Override
	public String getName() {
		return "SpriteRenderer";
	}
	
	public void setRegion(Vector2f regionOffset, Vector2f regionSize) {
		setRegion(regionOffset.x, regionOffset.y, regionSize.x, regionSize.y);
	}
	
	public void setRegion(float regX, float regY, float regWidth, float regHeight) {
		regionOffset.x = regX;
		regionOffset.y = regY;
		regionSize.x = regWidth;
		regionSize.y = regHeight;
		cropToRegion = true;
		updateMesh();
	}
	
	public Vector2f getOffset() {
		return mesh.getOrigin().shrink();
	}

	public void setOffset(Vector2f offset) {
		setOffset(offset.x, offset.y);
	}
	
	public void setOffset(float x, float y) {
		mesh.setOriginX(x);
		mesh.setOriginY(y);
	}
	
	public void unsetRegion() {
		cropToRegion = false;
		updateMesh();
	}
	
	private void updateMesh() {
		float w, h;
		if (cropToRegion) {
			w = regionSize.x;
			h = regionSize.y;
			uvModifier.setRegion(regionOffset, regionSize, textureSize);
		} else {
			w = textureSize.x;
			h = textureSize.y;
			uvModifier.resetRegion();
		}
		
		uvModifier.setFlipHorizontal(mirrorHorizontally);
		uvModifier.setFlipVertical(mirrorVertically);
		
		mesh.setSize(new Vector2f(w, h));
	}
}
