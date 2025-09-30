package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for images in the UI.
 */
@Components({ ImageNode.class, Node.class, Transform.class })
public class ImagePrefab extends NodePrefab {
	
	protected TextureBase image;
	
	protected boolean cropToRegion;
	protected int regionX;
	protected int regionY;
	protected int regionWidth;
	protected int regionHeight;
	
	public ImagePrefab(String imagePath) {
		this(AssetPools.textures.load(imagePath));
	}
	
	public ImagePrefab(TextureBase image) {
		this.image = image;
		
		cropToRegion = false;
		
		setName("Image");
		addClass("image");
		setHTMLTag("img");
	}
	
	public void setImage(TextureBase image) {
		this.image = image;
	}
	
	public void setRegion(int x, int y, int width, int height) {
		cropToRegion = true;
		regionX = x;
		regionY = y;
		regionWidth = width;
		regionHeight = height;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ImageNode imageNode = entity.addComponent(new ImageNode(image));
		
		// Crop image
		if (cropToRegion) {
			imageNode.regionOffset.x = regionX;
			imageNode.regionOffset.y = regionY;
			imageNode.regionSize.x = regionWidth;
			imageNode.regionSize.y = regionHeight;
		}
	}
}
