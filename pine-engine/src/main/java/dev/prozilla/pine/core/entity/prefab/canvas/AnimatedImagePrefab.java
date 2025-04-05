package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ImageAnimation;
import dev.prozilla.pine.core.entity.Entity;

public class AnimatedImagePrefab extends ImagePrefab {
	
	protected AdaptiveColorProperty colorProperty;
	
	public AnimatedImagePrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public AnimatedImagePrefab(TextureBase image) {
		super(image);
	}
	
	public void setColor(VariableProperty<Color> color) {
		setColor(new AdaptiveColorProperty(color));
	}
	
	public void setColor(AdaptiveColorProperty color) {
		colorProperty = color;
		this.color = color.getValue();
	}
	
	@Override
	public void setColor(Color color) {
		super.setColor(color);
		colorProperty = null;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ImageAnimation imageAnimation = entity.addComponent(new ImageAnimation());
		imageAnimation.setColorProperty(colorProperty);
	}
}
