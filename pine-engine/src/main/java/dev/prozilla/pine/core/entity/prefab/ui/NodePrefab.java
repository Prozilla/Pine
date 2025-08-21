package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyKey;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
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
	
	protected DualDimension size;
	protected DualDimension padding;
	protected DualDimension margin;
	protected Dimension border;
	protected TextureBase borderImage;
	protected Vector4f borderImageSlice;
	protected boolean borderImageSliceFill;
	protected Color color;
	protected Color backgroundColor;
	protected Color borderColor;
	protected GridAlignment anchor;
	protected boolean absolutePosition;
	protected boolean passThrough;
	protected String tooltipText;
	public int tabIndex;
	public boolean autoFocus;
	
	protected String htmlTag;
	protected Set<String> classes;
	
	protected StyleSheet styleSheet;
	
	public NodePrefab() {
		size = new DualDimension();
		absolutePosition = false;
		passThrough = false;
		tabIndex = -1;
		autoFocus = false;
		
		setName("CanvasElement");
	}
	
	/**
	 * Sets the style sheet that is applied to this node by loading it from a CSS file.
	 */
	public void setStyleSheet(String filePath) {
		setStyleSheet(AssetPools.styleSheets.load(filePath));
	}
	
	/**
	 * Sets the style sheet that is applied to this node.
	 */
	public void setStyleSheet(StyleSheet styleSheet) {
		this.styleSheet = styleSheet;
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
	public void setPadding(DimensionBase x, DimensionBase y) {
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
	
	public void setMargin(DimensionBase x, DimensionBase y) {
		setMargin(new DualDimension(x, y));
	}
	
	public void setMargin(DualDimension margin) {
		if (styleSheet == null) {
			this.margin = margin;
		} else {
			setPadding(AdaptiveDualDimensionProperty.adapt(margin));
		}
	}
	
	public void setMargin(VariableProperty<DualDimension> margin) {
		setDefaultPropertyValue(StyledPropertyKey.MARGIN, AdaptiveDualDimensionProperty.adapt(margin));
		this.margin = margin.getValue();
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
	
	public void setBorderColor(Color color) {
		borderColor = color;
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
	
	public void setBorder(Dimension border) {
		this.border = border;
	}
	
	public void setBorderImage(String borderImagePath, Vector4f slice, boolean fill) {
		setBorderImage(AssetPools.textures.load(borderImagePath), slice, fill);
	}
	
	public void setBorderImage(TextureBase borderImage, Vector4f slice, boolean fill) {
		this.borderImage = Checks.isNotNull(borderImage, "borderImage");
		borderImageSlice = Checks.isNotNull(slice, "slice");
		borderImageSliceFill = fill;
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
	
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	
	public void setAutoFocus(boolean autoFocus) {
		this.autoFocus = autoFocus;
		if (tabIndex < 0) {
			tabIndex = 0;
		}
	}
	
	public void setHTMLTag(String htmlTag) {
		this.htmlTag = htmlTag;
	}
	
	protected <T> void setDefaultPropertyValue(StyledPropertyKey<T> propertyName, AdaptiveProperty<T> value) {
		if (styleSheet == null) {
			return;
		}
		
		styleSheet.setDefaultValue(propertyName, value);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		Node node = entity.addComponent(new Node());
		node.size = size.clone();
		node.absolutePosition = absolutePosition;
		node.passThrough = passThrough;
		node.tabIndex = tabIndex;
		node.autoFocus = autoFocus;
		node.htmlTag = htmlTag;
		
		if (padding != null) {
			node.padding = padding.clone();
		}
		if (margin != null) {
			node.margin = margin.clone();
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
		if (border != null) {
			node.border = border.clone();
		}
		if (borderImage != null) {
			node.borderImage = borderImage;
			node.borderImageSlice = borderImageSlice.clone();
			node.borderImageSliceFill = borderImageSliceFill;
		}
		if (borderColor != null) {
			node.borderColor = borderColor;
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
