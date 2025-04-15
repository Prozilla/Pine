package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyKey;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.NodeStyle;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Prefab for UI elements.
 */
@Components({ Node.class, Transform.class })
public class NodePrefab extends Prefab {
	
	protected DualDimension position;
	protected DualDimension size;
	protected DualDimension padding;
	protected Color color;
	protected Color backgroundColor;
	protected GridAlignment anchor;
	protected boolean absolutePosition;
	protected boolean passThrough;
	protected String tooltipText;
	
	protected Set<String> classes;
	
	protected StyleSheet styleSheet;
	
	public NodePrefab() {
		position = new DualDimension();
		size = new DualDimension();
		absolutePosition = false;
		passThrough = false;
		
		setName("CanvasElement");
	}
	
	/**
	 * Sets the style sheet that is applied to this node by loading it from a CSS file.
	 */
	public void setStyleSheet(String filePath) {
		setStyleSheet(ResourcePool.loadStyleSheet(filePath));
	}
	
	/**
	 * Sets the style sheet that is applied to this node.
	 */
	public void setStyleSheet(StyleSheet styleSheet) {
		this.styleSheet = styleSheet;
	}
	
	/**
	 * Sets the position of this node relative to its anchor point.
	 */
	public void setPosition(DimensionBase x, DimensionBase y) {
		setPosition(new DualDimension(x, y));
	}
	
	/**
	 * Sets the position of this node relative to its anchor point.
	 */
	public void setPosition(DualDimension position) {
		if (styleSheet == null) {
			this.position = position;
		} else {
			setPosition(AdaptiveDualDimensionProperty.adapt(position));
		}
	}
	
	/**
	 * Sets the position of this node relative to its anchor point.
	 */
	public void setPosition(VariableProperty<DualDimension> position) {
		setDefaultPropertyValue(StyledPropertyKey.POSITION, AdaptiveDualDimensionProperty.adapt(position));
		this.position = position.getValue();
	}
	
	/**
	 * Sets the size of this node.
	 */
	public void setSize(DimensionBase x, DimensionBase y) {
		setSize(new DualDimension(x, y));
	}
	
	/**
	 * Sets the size of this node.
	 */
	public void setSize(DualDimension size) {
		if (styleSheet == null) {
			this.size = size;
		} else {
			setSize(AdaptiveDualDimensionProperty.adapt(size));
		}
	}
	
	/**
	 * Sets the size of this node.
	 */
	public void setSize(VariableProperty<DualDimension> size) {
		setDefaultPropertyValue(StyledPropertyKey.SIZE, AdaptiveDualDimensionProperty.adapt(size));
		this.size = size.getValue();
	}
	
	/**
	 * Sets the padding around the content of this node.
	 */
	public void setPadding(Dimension x, Dimension y) {
		setPadding(new DualDimension(x, y));
	}
	
	/**
	 * Sets the padding around the content of this node.
	 */
	public void setPadding(DualDimension padding) {
		if (styleSheet == null) {
			this.padding = padding;
		} else {
			setPadding(AdaptiveDualDimensionProperty.adapt(padding));
		}
	}
	
	/**
	 * Sets the padding around the content of this node.
	 */
	public void setPadding(VariableProperty<DualDimension> padding) {
		setDefaultPropertyValue(StyledPropertyKey.PADDING, AdaptiveDualDimensionProperty.adapt(padding));
		this.padding = padding.getValue();
	}
	
	/**
	 * Sets the foreground color of this node.
	 */
	public void setColor(Color color) {
		if (styleSheet == null) {
			this.color = color;
		} else {
			setColor(AdaptiveColorProperty.adapt(color));
		}
	}
	
	/**
	 * Sets the foreground color of this node.
	 */
	public void setColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyKey.COLOR, AdaptiveColorProperty.adapt(color));
		this.color = color.getValue();
	}
	
	/**
	 * Sets the background color of this node.
	 */
	public void setBackgroundColor(Color color) {
		if (styleSheet == null || color == null) {
			backgroundColor = color;
		} else {
			setBackgroundColor(AdaptiveColorProperty.adapt(color));
		}
	}
	
	/**
	 * Sets the background color of this node.
	 */
	public void setBackgroundColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyKey.BACKGROUND_COLOR, AdaptiveColorProperty.adapt(color));
		backgroundColor = color.getValue();
	}
	
	/**
	 * Sets the anchor point of this node.
	 */
	public void setAnchor(GridAlignment anchor) {
		this.anchor = anchor;
	}
	
	/**
	 * If <code>absolutePosition</code> is true, this node will not be affected by layout nodes.
	 */
	public void setAbsolutePosition(boolean absolutePosition) {
		this.absolutePosition = absolutePosition;
	}
	
	/**
	 * If <code>passThrough</code> is true, the cursor will not interact with this node.
	 */
	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}
	
	/**
	 * Sets the text that appears in a tooltip when the cursor hovers over this node.
	 */
	public void setTooltipText(String tooltipText) {
		this.tooltipText = tooltipText;
	}
	
	/**
	 * Adds multiple classes to this node.
	 */
	public void addClasses(String... classNames) {
		if (classes == null) {
			classes = new HashSet<>();
		}
		
		classes.addAll(List.of(classNames));
	}
	
	/**
	 * Adds a class to this node.
	 */
	public void addClass(String className) {
		if (classes == null) {
			classes = new HashSet<>();
		}
		
		classes.add(className);
	}
	
	protected <T> void setDefaultPropertyValue(StyledPropertyKey<T> propertyName, AdaptiveProperty<T> value) {
		if (styleSheet == null) {
			return;
		}
		
		styleSheet.setDefaultValue(propertyName, value);
	}
	
	@Override
	protected void apply(Entity entity) {
		Node node = entity.addComponent(new Node());
		node.position = position.clone();
		node.size = size.clone();
		node.absolutePosition = absolutePosition;
		node.passThrough = passThrough;
		
		if (padding != null) {
			node.padding = padding.clone();
		}
		if (color != null) {
			node.color = color.clone();
		}
		if (backgroundColor != null) {
			node.backgroundColor = backgroundColor.clone();
		}
		if (anchor != null) {
			node.anchor = anchor;
		}
		if (tooltipText != null) {
			node.tooltipText = tooltipText;
		}
		
		if (classes != null) {
			node.classes.addAll(classes);
		}
		
		if (styleSheet != null) {
			AnimationData animationData = entity.addComponent(new AnimationData(false));
			entity.addComponent(new NodeStyle(animationData, node, styleSheet));
		}
	}
}
