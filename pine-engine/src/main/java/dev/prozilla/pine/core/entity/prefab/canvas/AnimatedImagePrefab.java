package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyleColorProperty;
import dev.prozilla.pine.common.property.style.StyleRule;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ImageAnimation;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AnimatedImagePrefab extends ImagePrefab {
	
	protected final List<StyleRule<Color>> colorRules;
	protected AdaptiveColorProperty defaultColor;
	protected AdaptiveDualDimensionProperty sizeProperty;
	
	public AnimatedImagePrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public AnimatedImagePrefab(TextureBase image) {
		super(image);
		colorRules = new ArrayList<>();
	}
	
	public void addColorRule(StyleRule<Color> colorRule) {
		colorRules.add(colorRule);
	}
	
	@Override
	public void setColor(Color color) {
		setColor(new AdaptiveColorProperty(color));
	}
	
	public void setColor(VariableProperty<Color> color) {
		setColor(new AdaptiveColorProperty(color));
	}
	
	public void setColor(AdaptiveColorProperty color) {
		defaultColor = color;
		this.color = color.getValue().clone();
	}
	
	public void setSize(VariableProperty<DualDimension> size) {
		setSize(new AdaptiveDualDimensionProperty(size));
	}
	
	public void setSize(AdaptiveDualDimensionProperty size) {
		sizeProperty = size;
		this.size = size.getValue();
	}
	
	@Override
	public void setSize(DualDimension size) {
		super.setSize(size);
		sizeProperty = null;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		RectTransform rect = entity.getComponent(RectTransform.class);
		
		ImageAnimation imageAnimation = entity.addComponent(new ImageAnimation());
		imageAnimation.setColorProperty(new StyleColorProperty(rect, colorRules, defaultColor));
		imageAnimation.setSizeProperty(sizeProperty);
	}
}
