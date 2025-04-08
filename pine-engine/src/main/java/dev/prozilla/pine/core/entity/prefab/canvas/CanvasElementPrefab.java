package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyName;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.CanvasElementStyle;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for UI elements.
 */
@Components({ RectTransform.class, Transform.class })
public class CanvasElementPrefab extends Prefab {
	
	protected DualDimension position;
	protected DualDimension size;
	protected DualDimension padding;
	protected Color color;
	protected Color backgroundColor;
	protected GridAlignment anchor;
	protected boolean absolutePosition;
	protected boolean passThrough;
	protected String tooltipText;
	
	protected StyleSheet styleSheet;
	
	public CanvasElementPrefab() {
		position = new DualDimension();
		size = new DualDimension();
		absolutePosition = false;
		passThrough = false;
		
		setName("CanvasElement");
	}
	
	public void setStyleSheet(StyleSheet styleSheet) {
		this.styleSheet = styleSheet;
	}
	
	/**
	 * Sets the position of this element on the canvas relative to its anchor point.
	 */
	public void setPosition(DimensionBase x, DimensionBase y) {
		setPosition(new DualDimension(x, y));
	}
	
	/**
	 * Sets the position of this element on the canvas relative to its anchor point.
	 */
	public void setPosition(DualDimension position) {
		if (styleSheet == null) {
			this.position = position;
		} else {
			setPosition(AdaptiveDualDimensionProperty.adapt(position));
		}
	}
	
	public void setPosition(VariableProperty<DualDimension> position) {
		setDefaultPropertyValue(StyledPropertyName.POSITION, AdaptiveDualDimensionProperty.adapt(position));
		this.position = position.getValue();
	}
	
	/**
	 * Sets the size of this element on the canvas.
	 */
	public void setSize(DimensionBase x, DimensionBase y) {
		setSize(new DualDimension(x, y));
	}
	
	/**
	 * Sets the size of this element on the canvas.
	 */
	public void setSize(DualDimension size) {
		if (styleSheet == null) {
			this.size = size;
		} else {
			setSize(AdaptiveDualDimensionProperty.adapt(size));
		}
	}
	
	public void setSize(VariableProperty<DualDimension> size) {
		setDefaultPropertyValue(StyledPropertyName.SIZE, AdaptiveDualDimensionProperty.adapt(size));
		this.size = size.getValue();
	}
	
	public void setPadding(Dimension x, Dimension y) {
		setPadding(new DualDimension(x, y));
	}
	
	public void setPadding(DualDimension padding) {
		if (styleSheet == null) {
			this.padding = padding;
		} else {
			setPadding(AdaptiveDualDimensionProperty.adapt(padding));
		}
	}
	
	public void setPadding(VariableProperty<DualDimension> padding) {
		setDefaultPropertyValue(StyledPropertyName.PADDING, AdaptiveDualDimensionProperty.adapt(padding));
		this.padding = padding.getValue();
	}
	
	public void setColor(Color color) {
		if (styleSheet == null) {
			this.color = color;
		} else {
			setColor(AdaptiveColorProperty.adapt(color));
		}
	}
	
	public void setColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyName.COLOR, AdaptiveColorProperty.adapt(color));
		this.color = color.getValue().clone();
	}
	
	public void setBackgroundColor(Color color) {
		if (styleSheet == null || color == null) {
			backgroundColor = color;
		} else {
			setBackgroundColor(AdaptiveColorProperty.adapt(color));
		}
	}
	
	public void setBackgroundColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyName.BACKGROUND_COLOR, AdaptiveColorProperty.adapt(color));
		backgroundColor = color.getValue().clone();
	}
	
	/**
	 * Sets the anchor point on the canvas.
	 */
	public void setAnchor(GridAlignment anchor) {
		this.anchor = anchor;
	}
	
	public void setAbsolutePosition(boolean absolutePosition) {
		this.absolutePosition = absolutePosition;
	}
	
	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}
	
	public void setTooltipText(String tooltipText) {
		this.tooltipText = tooltipText;
	}
	
	protected <T> void setDefaultPropertyValue(StyledPropertyName propertyName, AdaptiveProperty<T> value) {
		if (styleSheet == null) {
			return;
		}
		
		styleSheet.setDefaultValue(propertyName, value);
	}
	
	@Override
	protected void apply(Entity entity) {
		RectTransform rectTransform = entity.addComponent(new RectTransform());
		rectTransform.position = position.clone();
		rectTransform.size = size.clone();
		rectTransform.absolutePosition = absolutePosition;
		rectTransform.passThrough = passThrough;
		
		if (padding != null) {
			rectTransform.padding = padding.clone();
		}
		if (color != null) {
			rectTransform.color = color.clone();
		}
		if (backgroundColor != null) {
			rectTransform.backgroundColor = backgroundColor.clone();
		}
		
		if (anchor != null) {
			rectTransform.anchor = anchor;
		}
		if (tooltipText != null) {
			rectTransform.tooltipText = tooltipText;
		}
		
		if (styleSheet != null) {
			entity.addComponent(new CanvasElementStyle(rectTransform, styleSheet));
		}
	}
}
