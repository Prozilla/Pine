package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.TextureBase;
import dev.prozilla.pine.core.component.Component;

import java.util.Objects;

/**
 * A component for rendering images on the canvas.
 */
public class ImageRenderer extends Component {
	
	public TextureBase image;
	public Color color;
	
	/** The size at which the image is rendered on the canvas. */
	public DualDimension size;
	
	public Vector2i regionOffset;
	public Vector2i regionSize;
	
	public ImageRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageRenderer(TextureBase image) {
		Objects.requireNonNull(image, "image must not be null");
		
		this.image = image;
		
		size = new DualDimension();
		regionOffset = new Vector2i(0, 0);
		regionSize = new Vector2i(image.getWidth(), image.getHeight());
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
	
	public void setSize(DualDimension size) {
		this.size = size;
	}
	
	public void setRegion(int regX, int regY, int regWidth, int regHeight) {
		regionOffset.x = regX;
		regionOffset.y = regY;
		regionSize.x = regWidth;
		regionSize.y = regHeight;
	}
}
