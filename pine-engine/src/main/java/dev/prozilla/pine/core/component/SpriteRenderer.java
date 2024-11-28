package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

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
	
	// Cropping
	/** Determines whether the texture will be cropped to a given region. */
	protected boolean cropToRegion;
	public float regionX;
	public float regionY;
	public float regionWidth;
	public float regionHeight;
	
	public SpriteRenderer(Texture texture) {
		this(texture, Color.WHITE.clone());
	}
	
	public SpriteRenderer(Texture texture, Color color) {
		this("SpriteRenderer", texture, color);
	}
	
	public SpriteRenderer(String name, Texture texture) {
		this(name, texture, Color.WHITE.clone());
	}
	
	public SpriteRenderer(String name, Texture texture, Color color) {
		super(name);
		
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
	public void render(Renderer renderer) {
		super.render(renderer);
		
		float[] position = gameObject.scene.camera.applyTransform(gameObject.x, gameObject.y);
		float x = position[0];
		float y = position[1];
		
		renderer.setScale(scale * gameObject.scene.camera.getZoom());
		
		if (!cropToRegion) {
			renderer.drawRotatedTexture(texture, x, y, color, rotation);
		} else {
			renderer.drawRotatedTextureRegion(texture, x, y,
				regionX, regionY, regionWidth, regionHeight, color, rotation);
		}
	}
	
	/**
	 * Crops this sprite to a given region.
	 */
	public void crop(float regX, float regY, float regWidth, float regHeight) {
		this.regionX = regX;
		this.regionY = regY;
		this.regionWidth = regWidth;
		this.regionHeight = regHeight;
		cropToRegion = true;
	}
	
	/**
	 * Disables cropping for this sprite.
	 */
	public void disableCropping() {
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