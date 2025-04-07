package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledColorProperty;
import dev.prozilla.pine.common.property.style.StyledDualDimensionProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that styles an {@link ImageRenderer} component.
 */
public class ImageStyler extends AnimationData {
	
	protected final RectTransform rect;
	protected StyledColorProperty colorProperty;
	protected StyledDualDimensionProperty sizeProperty;
	
	public ImageStyler(RectTransform rect) {
		this(rect, null);
	}
	
	public ImageStyler(RectTransform rect, StyleSheet styleSheet) {
		super(false);
		this.rect = rect;
		
		if (styleSheet != null) {
			applyStyleSheet(styleSheet);
		}
	}
	
	public void applyStyleSheet(StyleSheet styleSheet) {
		setColorProperty(styleSheet.createColorProperty(rect));
		setSizeProperty(styleSheet.createSizeProperty(rect));
	}
	
	public StyledColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyledColorProperty colorProperty) {
		changeProperty(this.colorProperty, colorProperty);
		this.colorProperty = colorProperty;
	}
	
	public StyledDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(StyledDualDimensionProperty sizeProperty) {
		changeProperty(this.sizeProperty, sizeProperty);
		this.sizeProperty = sizeProperty;
	}
	
}
