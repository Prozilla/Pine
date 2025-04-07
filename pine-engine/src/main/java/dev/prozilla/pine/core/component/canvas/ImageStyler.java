package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledColorProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that styles an {@link ImageRenderer} component.
 */
public class ImageStyler extends AnimationData {
	
	protected final RectTransform rect;
	protected StyledColorProperty colorProperty;
	protected AdaptiveDualDimensionProperty sizeProperty;
	
	public ImageStyler(RectTransform rect) {
		super(false);
		this.rect = rect;
	}
	
	public void applyStyleSheet(StyleSheet styleSheet) {
		setColorProperty(styleSheet.createColorProperty(rect));
	}
	
	public StyledColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyledColorProperty colorProperty) {
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
