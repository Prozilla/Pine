package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.ButtonNode;
import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

/**
 * Prefab for image buttons in the UI.
 */
@Components({ ButtonNode.class, ImageNode.class, Node.class, Transform.class })
public class ImageButtonPrefab extends ImagePrefab {
	
	protected ButtonNode.ClickCallback clickCallback;
	
	public ImageButtonPrefab(String imagePath) {
		this(AssetPools.textures.load(imagePath));
	}
	
	public ImageButtonPrefab(TextureBase image) {
		super(image);
		setName("ImageButton");
		addClass("button");
		setTabIndex(0);
	}
	
	public void setClickCallback(ButtonNode.ClickCallback callback) {
		clickCallback = callback;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ButtonNode buttonNode = entity.addComponent(new ButtonNode());
		
		if (clickCallback != null) {
			buttonNode.clickCallback = clickCallback;
		}
	}
}
