package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyName;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.CanvasElementStyler;
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
		this.position = position;
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
			setSize(new AdaptiveDualDimensionProperty(size));
		}
	}
	
	public void setSize(VariableProperty<DualDimension> size) {
		setDefaultPropertyValue(StyledPropertyName.SIZE, size);
		this.size = size.getValue();
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
	
	protected <T> void setDefaultPropertyValue(StyledPropertyName propertyName, VariableProperty<T> defaultValue) {
		if (styleSheet == null) {
			return;
		}
		
		styleSheet.setDefaultValue(propertyName, defaultValue instanceof AdaptiveProperty<T> adaptiveProperty ? adaptiveProperty : new AdaptiveProperty<>(defaultValue));
	}
	
	@Override
	protected void apply(Entity entity) {
		RectTransform rectTransform = entity.addComponent(new RectTransform());
		rectTransform.setPosition(position.clone());
		rectTransform.setSize(size.clone());
		rectTransform.absolutePosition = absolutePosition;
		rectTransform.passThrough = passThrough;
		
		if (anchor != null) {
			rectTransform.setAnchor(anchor);
		}
		if (tooltipText != null) {
			rectTransform.tooltipText = tooltipText;
		}
		
		if (styleSheet != null) {
			entity.addComponent(new CanvasElementStyler(rectTransform, styleSheet));
		}
	}
}
