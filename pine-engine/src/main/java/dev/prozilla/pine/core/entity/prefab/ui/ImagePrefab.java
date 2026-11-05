package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.DynamicImage;
import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for images in the UI.
 */
@Components({ ImageNode.class, Node.class, Transform.class })
public class ImagePrefab extends NodePrefab {
	
	protected TextureAsset image;
	protected AdaptiveObjectProperty<TextureAsset> imageProperty;
	
	protected boolean cropToRegion;
	protected int regionX;
	protected int regionY;
	protected int regionWidth;
	protected int regionHeight;
	
	public ImagePrefab(Property<TextureAsset> imageProperty) {
		this(imageProperty.getValue());
		setImage(imageProperty);
	}
	
	public ImagePrefab(String imagePath) {
		this(AssetPools.textures.load(imagePath));
	}
	
	public ImagePrefab(TextureAsset image) {
		this.image = image;
		
		cropToRegion = false;
		
		setName("Image");
		addClass("image");
		setHTMLTag(Node.IMAGE_TAG);
	}
	
	public void setImage(Property<TextureAsset> imageProperty) {
		this.imageProperty = AdaptiveObjectProperty.adapt(imageProperty);
	}
	
	public void setImage(TextureAsset image) {
		this.image = image;
		imageProperty = null;
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
		
		if (imageProperty != null) {
			entity.addComponent(new DynamicImage(imageProperty));
		}
		
		// Crop image
		if (cropToRegion) {
			imageNode.regionOffset.x = regionX;
			imageNode.regionOffset.y = regionY;
			imageNode.regionSize.x = regionWidth;
			imageNode.regionSize.y = regionHeight;
		}
	}
}
