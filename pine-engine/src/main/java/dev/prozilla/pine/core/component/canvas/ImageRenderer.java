package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector4i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering images on the canvas.
 */
public class ImageRenderer extends Component {
	
	public Texture image;
	public Color color;
	
	/** The size at which the image is rendered on the canvas. */
	public Vector2i size;
	
	public Vector2i regionOffset;
	public Vector2i regionSize;
	
	public ImageRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageRenderer(Texture image) {
		if (image == null) {
			throw new IllegalArgumentException("Image can't be null");
		}
		
		this.image = image;
		
		size = new Vector2i();
		regionOffset = new Vector2i();
		regionSize = new Vector2i();
	}
	
	@Override
	public String getName() {
		return "ImageRenderer";
	}
	
	public void setImage(Texture image) {
		this.image = image;
		regionOffset.x = 0;
		regionOffset.y = 0;
		regionSize.x = image.getWidth();
		regionSize.y = image.getHeight();
	}
	
	public void setSize(int width, int height) {
		size.x = width;
		size.y = height;
	}
	
	public void setRegion(int regX, int regY, int regWidth, int regHeight) {
		regionOffset.x = regX;
		regionOffset.y = regY;
		regionSize.x = regWidth;
		regionSize.y = regHeight;
	}
}
