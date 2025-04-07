package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyName;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ImageStyler;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;

public class StyledImagePrefab extends ImagePrefab {
	
	protected StyleSheet styleSheet;
	
	public StyledImagePrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public StyledImagePrefab(TextureBase image) {
		super(image);
	}
	
	public void setStyleSheet(StyleSheet styleSheet) {
		this.styleSheet = styleSheet;
	}
	
	@Override
	public void setColor(Color color) {
		setColor(new AdaptiveColorProperty(color));
	}
	
	public void setColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyName.COLOR, color);
		this.color = color.getValue().clone();
	}
	
	@Override
	public void setSize(DualDimension size) {
		setSize(new AdaptiveDualDimensionProperty(size));
	}
	
	public void setSize(VariableProperty<DualDimension> size) {
		setDefaultPropertyValue(StyledPropertyName.SIZE, size);
		this.size = size.getValue();
	}
	
	protected <T> void setDefaultPropertyValue(StyledPropertyName propertyName, VariableProperty<T> defaultValue) {
		if (styleSheet == null) {
			return;
		}
		
		styleSheet.setDefaultValue(propertyName, defaultValue instanceof AdaptiveProperty<T> adaptiveProperty ? adaptiveProperty : new AdaptiveProperty<>(defaultValue));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		RectTransform rect = entity.getComponent(RectTransform.class);
		entity.addComponent(new ImageStyler(rect, styleSheet));
	}
}
