package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for image buttons in the UI.
 */
public class ImageButtonPrefab extends ImagePrefab {
	
	protected ButtonData.ClickCallback clickCallback;
	
	public ImageButtonPrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonPrefab(TextureBase image) {
		super(image);
		setName("ImageButton");
	}
	
	public void setClickCallback(ButtonData.ClickCallback callback) {
		clickCallback = callback;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ButtonData buttonData = entity.addComponent(new ButtonData());
		
		if (clickCallback != null) {
			buttonData.clickCallback = clickCallback;
		}
	}
}
