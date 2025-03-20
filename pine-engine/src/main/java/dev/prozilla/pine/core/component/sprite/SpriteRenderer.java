package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.TextureBase;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering 2D sprites in the world.
 */
public class SpriteRenderer extends Component {
	
	// Visual properties
	public TextureBase texture;
	public final Color color;
	
	// Transformations
	public float scale;
	public float rotation;
	public Vector2f offset;
	public boolean mirrorHorizontally;
	public boolean mirrorVertically;
	
	// Cropping
	/** Determines whether the texture will be cropped to a given region. */
	public boolean cropToRegion;
	public Vector2f regionOffset;
	public Vector2f regionSize;
	
	public SpriteRenderer(TextureBase texture) {
		this(texture, Color.white());
	}
	
	public SpriteRenderer(TextureBase texture, Color color) {
		this.texture = texture;
		this.color = color;
		
		scale = 1f;
		rotation = 0f;
		offset = new Vector2f();
		mirrorHorizontally = false;
		mirrorVertically = false;
		
		cropToRegion = false;
		regionOffset = new Vector2f();
		regionSize = new Vector2f(texture.getWidth(), texture.getHeight());
	}
	
	@Override
	public String getName() {
		return "SpriteRenderer";
	}
	
	public void setRegion(Vector2f regionOffset, Vector2f regionSize) {
		setRegion(regionOffset.x, regionOffset.y, regionSize.x, regionSize.y);
	}
	
	/**
	 * Crops this sprite to a given region.
	 */
	public void setRegion(float regX, float regY, float regWidth, float regHeight) {
		regionOffset.x = regX;
		regionOffset.y = regY;
		regionSize.x = regWidth;
		regionSize.y = regHeight;
		cropToRegion = true;
	}
	
	/**
	 * Disables cropping for this sprite.
	 */
	public void unsetRegion() {
		cropToRegion = false;
	}
	
	// TO DO: apply rotation
	public float getWidth() {
		if (cropToRegion) {
			return regionSize.x * scale;
		} else {
			return (float)texture.getWidth() * scale;
		}
	}
	
	// TO DO: apply rotation
	public float getHeight() {
		if (cropToRegion) {
			return regionSize.y * scale;
		} else {
			return (float)texture.getHeight() * scale;
		}
	}
	
	public float getX() {
		return entity.transform.getGlobalX() + offset.x;
	}
	
	public float getY() {
		return entity.transform.getGlobalY() + offset.y;
	}
}
