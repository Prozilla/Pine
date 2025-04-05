package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that animates an {@link ImageRenderer} component.
 */
public class ImageAnimation extends AnimationData {

	protected AdaptiveColorProperty colorProperty;
	protected AdaptiveDualDimensionProperty sizeProperty;
	
	public ImageAnimation() {
		super(false);
	}
	
	public AdaptiveColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(AdaptiveColorProperty colorProperty) {
		changeProperty(this.colorProperty, colorProperty);
		this.colorProperty = colorProperty;
	}
	
	public AdaptiveDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(AdaptiveDualDimensionProperty sizeProperty) {
		changeProperty(this.sizeProperty, sizeProperty);
		this.sizeProperty = sizeProperty;
	}
	
}
