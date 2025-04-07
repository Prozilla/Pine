package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyleColorProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that animates an {@link ImageRenderer} component.
 */
public class ImageAnimation extends AnimationData {

	protected StyleColorProperty colorProperty;
	protected AdaptiveDualDimensionProperty sizeProperty;
	
	public ImageAnimation() {
		super(false);
	}
	
	public StyleColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyleColorProperty colorProperty) {
		changeProperty(this.colorProperty, colorProperty);
		this.colorProperty = colorProperty;
		colorProperty.print();
	}
	
	public AdaptiveDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(AdaptiveDualDimensionProperty sizeProperty) {
		changeProperty(this.sizeProperty, sizeProperty);
		this.sizeProperty = sizeProperty;
	}
	
}
