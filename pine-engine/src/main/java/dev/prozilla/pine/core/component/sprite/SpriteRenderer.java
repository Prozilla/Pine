package dev.prozilla.pine.core.component.sprite;

import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering 2D sprites in the world.
 */
public class SpriteRenderer extends Component {
	
	// Visual properties
	public Texture texture;
	public final Color color;
	
	// Transformations
	public float scale;
	public float rotation;
	/** Horizontal offset for this sprite, in pixels. */
	public float offsetX;
	/** Vertical offset for this sprite, in pixels. */
	public float offsetY;
	
	// Cropping
	/** Determines whether the texture will be cropped to a given region. */
	public boolean cropToRegion;
	public float regionX;
	public float regionY;
	public float regionWidth;
	public float regionHeight;
	
	public SpriteRenderer(Texture texture) {
		this(texture, Color.WHITE.clone());
	}
	
	public SpriteRenderer(Texture texture, Color color) {
		this.texture = texture;
		this.color = color;
		
		scale = 1f;
		rotation = 0f;
		
		cropToRegion = false;
		regionX = 0;
		regionY = 0;
		regionWidth = texture.getWidth();
		regionHeight = texture.getHeight();
	}
	
	@Override
	public String getName() {
		return "SpriteRenderer";
	}
	
	/**
	 * Crops this sprite to a given region.
	 */
	public void setRegion(float regX, float regY, float regWidth, float regHeight) {
		this.regionX = regX;
		this.regionY = regY;
		this.regionWidth = regWidth;
		this.regionHeight = regHeight;
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
			return regionWidth * scale;
		} else {
			return (float)texture.getWidth() * scale;
		}
	}
	
	// TO DO: apply rotation
	public float getHeight() {
		if (cropToRegion) {
			return regionHeight * scale;
		} else {
			return (float)texture.getHeight() * scale;
		}
	}
}
