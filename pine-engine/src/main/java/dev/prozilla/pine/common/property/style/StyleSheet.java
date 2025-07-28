package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.adaptive.*;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Manages style rules for different properties of nodes.
 */
public class StyleSheet implements Printable, Asset {
	
	private final Map<StyledPropertyKey<?>, Style<?>> styles;
	
	public String path;
	
	public StyleSheet() {
		this.styles = new HashMap<>();
	}
	
	public <T> void parseRule(Selector selector, StyledPropertyKey<T> propertyName, String input) {
		T value = null;
		
		try {
			value = propertyName.parseValue(input);
		} catch (Exception ignored) {}
		
		if (value != null) {
			addRule(selector, propertyName, value);
		}
	}
	
	public <T> void addRule(Selector selector, StyledPropertyKey<T> propertyName, T value) {
		addRule(propertyName, new StyleRule<>(selector, value));
	}
	
	protected <T> void addRule(StyledPropertyKey<T> propertyName, StyleRule<T> rule) {
		Style<T> style = getStyle(propertyName, true);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyKey<?> propertyName, AnimationCurve value) {
		addTransition(propertyName, new StyleRule<>(selector, value));
	}
	
	protected void addTransition(StyledPropertyKey<?> propertyName, StyleRule<AnimationCurve> transitionRule) {
		Style<?> style = getStyle(propertyName, true);
		style.addTransitionRule(transitionRule);
	}
	
	public <T> void setDefaultValue(StyledPropertyKey<T> propertyName, AdaptiveProperty<T> defaultValue) {
		Objects.requireNonNull(defaultValue, "defaultValue must not be null");
		
		Style<T> style = getStyle(propertyName, true);
		style.setDefaultValue(defaultValue);
	}
	
	public StyledColorProperty createColorProperty(Node node) {
		return createStyledColorProperty(StyledPropertyKey.COLOR, node, Node.DEFAULT_COLOR.clone());
	}
	
	public StyledColorProperty createBackgroundColorProperty(Node node) {
		return createStyledColorProperty(StyledPropertyKey.BACKGROUND_COLOR, node, Node.DEFAULT_BACKGROUND_COLOR.clone());
	}
	
	public StyledDualDimensionProperty createSizeProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.SIZE, node, new DualDimension());
	}
	
	public StyledDualDimensionProperty createPaddingProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.PADDING, node, new DualDimension());
	}
	
	public StyledDualDimensionProperty createMarginProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.MARGIN, node, new DualDimension());
	}
	
	public StyledGridAlignmentProperty createAnchorProperty(Node node) {
		return createStyledGridAlignmentProperty(StyledPropertyKey.ANCHOR, node, Node.DEFAULT_ANCHOR);
	}
	
	public StyledDimensionProperty createGapProperty(Node node) {
		return createStyledDimensionProperty(StyledPropertyKey.GAP, node, LayoutNode.DEFAULT_GAP);
	}
	
	public StyledDirectionProperty createDirectionProperty(Node node) {
		return createStyledDirectionProperty(StyledPropertyKey.DIRECTION, node, LayoutNode.DEFAULT_DIRECTION);
	}
	
	public StyledEdgeAlignmentProperty createAlignmentProperty(Node node) {
		return createStyledEdgeAlignmentProperty(StyledPropertyKey.ALIGNMENT, node, LayoutNode.DEFAULT_ALIGNMENT);
	}
	
	public StyledDistributionProperty createDistributionProperty(Node node) {
		return createStyledDistributionProperty(StyledPropertyKey.DISTRIBUTION, node, LayoutNode.DEFAULT_DISTRIBUTION);
	}
	
	protected StyledColorProperty createStyledColorProperty(StyledPropertyKey<Color> name, Node node, Color fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveColorProperty(fallbackValue),  (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	protected StyledDimensionProperty createStyledDimensionProperty(StyledPropertyKey<DimensionBase> name, Node node, DimensionBase fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveDimensionProperty(fallbackValue),  (Style.StyledPropertyFactory<DimensionBase, StyledDimensionProperty>)StyledDimensionProperty::new);
	}
	
	protected StyledDualDimensionProperty createStyledDualDimensionProperty(StyledPropertyKey<DualDimension> name, Node node, DualDimension fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveDualDimensionProperty(fallbackValue),  (Style.StyledPropertyFactory<DualDimension, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	protected StyledGridAlignmentProperty createStyledGridAlignmentProperty(StyledPropertyKey<GridAlignment> name, Node node, GridAlignment fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveGridAlignmentProperty(fallbackValue),  (Style.StyledPropertyFactory<GridAlignment, StyledGridAlignmentProperty>)StyledGridAlignmentProperty::new);
	}
	
	protected StyledIntProperty createStyledIntProperty(StyledPropertyKey<Integer> name, Node node, int fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveIntProperty(fallbackValue),  (Style.StyledPropertyFactory<Integer, StyledIntProperty>)StyledIntProperty::new);
	}
	
	protected StyledDirectionProperty createStyledDirectionProperty(StyledPropertyKey<Direction> name, Node node, Direction fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveDirectionProperty(fallbackValue),  (Style.StyledPropertyFactory<Direction, StyledDirectionProperty>)StyledDirectionProperty::new);
	}
	
	protected StyledEdgeAlignmentProperty createStyledEdgeAlignmentProperty(StyledPropertyKey<EdgeAlignment> name, Node node, EdgeAlignment fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveEdgeAlignmentProperty(fallbackValue),  (Style.StyledPropertyFactory<EdgeAlignment, StyledEdgeAlignmentProperty>)StyledEdgeAlignmentProperty::new);
	}
	
	protected StyledDistributionProperty createStyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> name, Node node, LayoutNode.Distribution fallbackValue) {
		return createStyledProperty(name, node, new AdaptiveDistributionProperty(fallbackValue),  (Style.StyledPropertyFactory<LayoutNode.Distribution, StyledDistributionProperty>)StyledDistributionProperty::new);
	}
	
	protected  <T, P extends StyledProperty<T>> P createStyledProperty(StyledPropertyKey<T> name, Node node, AdaptivePropertyBase<T> fallbackValue, Style.StyledPropertyFactory<T, P> factory) {
		Style<T> style = getStyle(name, false);
		return style != null ? style.toProperty(name, node, fallbackValue, factory) : null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Style<T> getStyle(StyledPropertyKey<T> propertyName, boolean createIfMissing) {
		return (Style<T>)getGenericStyle(propertyName, createIfMissing);
	}
	
	protected Style<?> getGenericStyle(StyledPropertyKey<?> propertyName, boolean createIfMissing) {
		Style<?> style = styles.get(propertyName);
		
		if (style == null && createIfMissing) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void destroy() {
		AssetPools.styleSheets.remove(this);
	}
	
	@Override
	public String toString() {
		Map<String, StringJoiner> selectorToProperties = new HashMap<>();
		
		for (Map.Entry<StyledPropertyKey<?>, Style<?>> styleEntry : styles.entrySet()) {
			StyledPropertyKey<?> propertyName = styleEntry.getKey();
			Style<?> style = styleEntry.getValue();
			
			for (StyleRule<?> rule : style.getRules()) {
				String selector = rule.selector().toString();
				String property = String.format("%s: %s;", propertyName.toString(), rule.value().toString());
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(" "))
					.add(property);
			}
			
			for (StyleRule<AnimationCurve> transitionRule : style.getTransitionRules()) {
				String selector = transitionRule.selector().toString();
				String property = String.format("transition: %s %s;", propertyName.toString(), transitionRule.value().toString());
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(" "))
					.add(property);
			}
		}
		
		StringJoiner result = new StringJoiner(" ");
		for (Map.Entry<String, StringJoiner> entry : selectorToProperties.entrySet()) {
			result.add(entry.getKey());
			result.add("{");
			result.add(entry.getValue().toString());
			result.add("}");
		}
		
		return result.toString();
	}
	
	/**
	 * @deprecated Replaced by {@link CSSParser} as of 1.2.0
	 */
	@Deprecated
	public static StyleSheet parse(String css) {
		return new CSSParser().read(css);
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof StyleSheet styleSheet && equals(styleSheet));
	}
	
	public boolean equals(StyleSheet styleSheet) {
		return styleSheet != null && Objects.equals(styles, styleSheet.styles);
	}
	
}
