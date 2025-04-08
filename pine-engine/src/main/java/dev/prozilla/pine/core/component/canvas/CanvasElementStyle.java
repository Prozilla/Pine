package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledColorProperty;
import dev.prozilla.pine.common.property.style.StyledDualDimensionProperty;
import dev.prozilla.pine.common.property.style.StyledProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that styles canvas elements.
 */
public class CanvasElementStyle extends AnimationData {
	
	protected final RectTransform rect;
	protected StyledColorProperty colorProperty;
	protected StyledColorProperty backgroundColorProperty;
	protected StyledDualDimensionProperty sizeProperty;
	
	public CanvasElementStyle(RectTransform rect) {
		this(rect, null);
	}
	
	public CanvasElementStyle(RectTransform rect, StyleSheet styleSheet) {
		super(false);
		this.rect = rect;
		
		if (styleSheet != null) {
			applyStyleSheet(styleSheet);
		}
	}
	
	public void applyStyleSheet(StyleSheet styleSheet) {
		setColorProperty(styleSheet.createColorProperty(rect));
		setBackgroundColorProperty(styleSheet.createBackgroundColorProperty(rect));
		setSizeProperty(styleSheet.createSizeProperty(rect));
	}
	
	public StyledColorProperty getColorProperty() {
		return colorProperty;
	}
	
	public void setColorProperty(StyledColorProperty colorProperty) {
		changeProperty(this.colorProperty, colorProperty);
		this.colorProperty = colorProperty;
	}
	
	public StyledColorProperty getBackgroundColorProperty() {
		return backgroundColorProperty;
	}
	
	public void setBackgroundColorProperty(StyledColorProperty backgroundColorProperty) {
		changeProperty(this.backgroundColorProperty, backgroundColorProperty);
		this.backgroundColorProperty = backgroundColorProperty;
	}
	
	public StyledDualDimensionProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public void setSizeProperty(StyledDualDimensionProperty sizeProperty) {
		changeProperty(this.sizeProperty, sizeProperty);
		this.sizeProperty = sizeProperty;
	}
	
	protected void changeProperty(StyledProperty<?> oldProperty, StyledProperty<?> newProperty) {
		super.changeProperty(oldProperty, newProperty);
		if (newProperty != null) {
			newProperty.invalidate();
		}
	}
}
