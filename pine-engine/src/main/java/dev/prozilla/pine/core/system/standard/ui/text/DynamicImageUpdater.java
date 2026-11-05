package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.core.component.ui.DynamicImage;
import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

import java.util.Objects;

public final class DynamicImageUpdater extends UpdateSystem {
	
	public DynamicImageUpdater() {
		super(ImageNode.class, DynamicImage.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageNode imageNode = chunk.getComponent(ImageNode.class);
		DynamicImage dynamicImage = chunk.getComponent(DynamicImage.class);
		
		TextureAsset newImage = dynamicImage.imageProperty.getValue();
		
		if (newImage != null && !Objects.equals(imageNode.image, newImage)) {
			imageNode.setImage(newImage);
		}
	}
}
