package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering an image in the UI.
 */
public class ImageNode extends Component {
	
	public TextureBase image;
	
	public Vector2i regionOffset;
	public Vector2i regionSize;
	
	public ImageNode(String imagePath) {
		this(AssetPools.textures.load(imagePath));
	}
	
	public ImageNode(TextureBase image) {
		Checks.isNotNull(image, "image");
		
		this.image = image;
		
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
	
	public void setRegion(int regX, int regY, int regWidth, int regHeight) {
		regionOffset.x = regX;
		regionOffset.y = regY;
		regionSize.x = regWidth;
		regionSize.y = regHeight;
	}
}
