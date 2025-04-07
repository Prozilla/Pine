package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.style.StyledPropertyName;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for image buttons in the UI.
 */
public class ImageButtonPrefab extends ImagePrefab {
	
	protected Color backgroundColor;
	protected DualDimension padding;
	
	protected ButtonData.ClickCallback clickCallback;
	
	public ImageButtonPrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonPrefab(TextureBase image) {
		super(image);
		setName("ImageButton");
		
		padding = new DualDimension();
		backgroundColor = ImageButtonRenderer.DEFAULT_BACKGROUND_COLOR;
	}
	
	public void setPadding(Dimension x, Dimension y) {
		setPadding(new DualDimension(x, y));
	}
	
	public void setPadding(DualDimension padding) {
		this.padding = padding;
	}
	
	public void setClickCallback(ButtonData.ClickCallback callback) {
		clickCallback = callback;
	}
	
	public void setBackgroundColor(Color color) {
		if (styleSheet == null || color == null) {
			backgroundColor = color;
		} else {
			setBackgroundColor(new AdaptiveColorProperty(color));
		}
	}
	
	public void setBackgroundColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyName.BACKGROUND_COLOR, color);
		backgroundColor = color.getValue().clone();
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ImageButtonRenderer imageButtonRenderer = entity.addComponent(new ImageButtonRenderer());
		
		imageButtonRenderer.padding = padding;
		imageButtonRenderer.backgroundColor = backgroundColor;
		
		ButtonData buttonData = entity.addComponent(new ButtonData());
		
		if (clickCallback != null) {
			buttonData.clickCallback = clickCallback;
		}
	}
}
