package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.Transceivable;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.Unit;
import dev.prozilla.pine.common.math.vector.Alignment;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveIntProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.ModifierSelector;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.property.style.selector.SelectorCombo;
import dev.prozilla.pine.common.property.style.selector.TypeSelector;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.BorderStyle;
import dev.prozilla.pine.core.state.input.CursorType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Manages style rules for different properties of nodes.
 */
public class StyleSheet implements Printable, Asset, Transceivable<StyleSheet> {
	
	private final Map<StyledPropertyKey<?>, Style<?, ?>> styles;
	
	public String path;
	
	public static final StyleSheet DEFAULT = createDefault();
	
	public StyleSheet() {
		this(null);
	}
	
	public StyleSheet(String path) {
		this.path = path;
		styles = new HashMap<>();
	}
	
	public <T> void parseRule(Selector selector, StyledPropertyKey<T> key, String input) {
		T value = null;
		
		try {
			value = key.parseValue(input);
		} catch (Exception ignored) {}
		
		if (value != null) {
			addRule(selector, key, value);
		}
	}
	
	public <T> void addRule(Selector selector, StyledPropertyKey<T> key, T value) {
		addRule(selector, key, value, false);
	}
	
	protected <T> void addDefaultRule(Selector selector, StyledPropertyKey<T> key, T value) {
		addRule(selector, key, value, true);
	}
	
	protected <T> void addRule(Selector selector, StyledPropertyKey<T> key, T value, boolean isDefault) {
		addRule(key, new StyleRule<>(selector, value, isDefault));
	}
	
	protected <T> void addRule(StyledPropertyKey<T> key, StyleRule<T> rule) {
		Style<T, ?> style = getStyle(key, true);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyKey<?> key, AnimationCurve value) {
		addTransition(selector, key, value, false);
	}
	
	protected void addDefaultTransition(Selector selector, StyledPropertyKey<?> key, AnimationCurve value) {
		addTransition(selector, key, value, true);
	}
	
	protected void addTransition(Selector selector, StyledPropertyKey<?> key, AnimationCurve value, boolean isDefault) {
		addTransition(key, new StyleRule<>(selector, value, isDefault));
	}
	
	protected void addTransition(StyledPropertyKey<?> key, StyleRule<AnimationCurve> transitionRule) {
		Style<?, ?> style = getStyle(key, true);
		style.addTransitionRule(transitionRule);
	}
	
	public <T, A extends AdaptiveProperty<T, ?>> void setDefaultValue(StyledPropertyKey<T> key, A defaultValue) {
		Checks.isNotNull(defaultValue, "defaultValue");
		
		Style<T, A> style = getStyle(key, true, defaultValue.getClass());
		style.setDefaultValue(defaultValue);
	}
	
	@Contract("_ -> new")
	public StyledColorProperty createColorProperty(Node node) {
		return createStyledColorProperty(StyledPropertyKey.COLOR, node, Node.DEFAULT_COLOR.clone());
	}
	
	@Contract("_ -> new")
	public StyledColorProperty createBackgroundColorProperty(Node node) {
		return createStyledColorProperty(StyledPropertyKey.BACKGROUND_COLOR, node, Node.DEFAULT_BACKGROUND_COLOR.clone());
	}
	
	@Contract("_ -> new")
	public StyledDualDimensionProperty createSizeProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.SIZE, node, new DualDimension());
	}
	
	@Contract("_ -> new")
	public StyledDualDimensionProperty createPaddingProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.PADDING, node, new DualDimension());
	}
	
	@Contract("_ -> new")
	public StyledDualDimensionProperty createMarginProperty(Node node) {
		return createStyledDualDimensionProperty(StyledPropertyKey.MARGIN, node, new DualDimension());
	}
	
	@Contract("_ -> new")
	public StyledGridAlignmentProperty createAnchorProperty(Node node) {
		return createStyledGridAlignmentProperty(StyledPropertyKey.ANCHOR, node, Node.DEFAULT_ANCHOR);
	}
	
	@Contract("_ -> new")
	public StyledDimensionProperty createGapProperty(Node node) {
		return createStyledDimensionProperty(StyledPropertyKey.GAP, node, LayoutNode.DEFAULT_GAP);
	}
	
	@Contract("_ -> new")
	public StyledDirectionProperty createDirectionProperty(Node node) {
		return createStyledDirectionProperty(StyledPropertyKey.DIRECTION, node, LayoutNode.DEFAULT_DIRECTION);
	}
	
	@Contract("_ -> new")
	public StyledEdgeAlignmentProperty createAlignmentProperty(Node node) {
		return createStyledEdgeAlignmentProperty(StyledPropertyKey.ALIGNMENT, node, LayoutNode.DEFAULT_ALIGNMENT);
	}
	
	@Contract("_ -> new")
	public StyledDistributionProperty createDistributionProperty(Node node) {
		return createStyledDistributionProperty(StyledPropertyKey.DISTRIBUTION, node, LayoutNode.DEFAULT_DISTRIBUTION);
	}
	
	@Contract("_ -> new")
	public StyledCursorProperty createCursorProperty(Node node) {
		return createStyledCursorProperty(StyledPropertyKey.CURSOR, node, CursorType.DEFAULT);
	}
	
	@Contract("_ -> new")
	public StyledDimensionProperty createBorderWidthProperty(Node node) {
		return createStyledDimensionProperty(StyledPropertyKey.BORDER_WIDTH, node, Dimension.zero());
	}
	
	@Contract("_ -> new")
	public StyledBorderStyleProperty createBorderStyleProperty(Node node) {
		return createStyledBorderStyleProperty(StyledPropertyKey.BORDER_STYLE, node, BorderStyle.NONE);
	}
	
	protected StyledColorProperty createStyledColorProperty(StyledPropertyKey<Color> key, Node node, Color fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveColorProperty(fallbackValue),  (Style.StyledPropertyFactory<Color, AdaptiveObjectProperty<Color>, StyledColorProperty>)StyledColorProperty::new);
	}
	
	protected StyledDimensionProperty createStyledDimensionProperty(StyledPropertyKey<DimensionBase> key, Node node, DimensionBase fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<DimensionBase, AdaptiveObjectProperty<DimensionBase>, StyledDimensionProperty>)StyledDimensionProperty::new);
	}
	
	protected StyledDualDimensionProperty createStyledDualDimensionProperty(StyledPropertyKey<DualDimension> key, Node node, DualDimension fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<DualDimension, AdaptiveObjectProperty<DualDimension>, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	protected StyledGridAlignmentProperty createStyledGridAlignmentProperty(StyledPropertyKey<Anchor> key, Node node, Anchor fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<Anchor, AdaptiveObjectProperty<Anchor>, StyledGridAlignmentProperty>)StyledGridAlignmentProperty::new);
	}
	
	protected StyledIntProperty createStyledIntProperty(StyledPropertyKey<Integer> key, Node node, int fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveIntProperty(fallbackValue),  (Style.StyledPropertyFactory<Integer, AdaptiveIntProperty, StyledIntProperty>)StyledIntProperty::new);
	}
	
	protected StyledDirectionProperty createStyledDirectionProperty(StyledPropertyKey<Direction> key, Node node, Direction fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<Direction, AdaptiveObjectProperty<Direction>, StyledDirectionProperty>)StyledDirectionProperty::new);
	}
	
	protected StyledEdgeAlignmentProperty createStyledEdgeAlignmentProperty(StyledPropertyKey<Alignment> key, Node node, Alignment fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<Alignment, AdaptiveObjectProperty<Alignment>, StyledEdgeAlignmentProperty>)StyledEdgeAlignmentProperty::new);
	}
	
	protected StyledDistributionProperty createStyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> key, Node node, LayoutNode.Distribution fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<LayoutNode.Distribution, AdaptiveObjectProperty<LayoutNode.Distribution>, StyledDistributionProperty>)StyledDistributionProperty::new);
	}
	
	protected StyledCursorProperty createStyledCursorProperty(StyledPropertyKey<CursorType> key, Node node, CursorType fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<CursorType, AdaptiveObjectProperty<CursorType>, StyledCursorProperty>)StyledCursorProperty::new);
	}
	
	protected StyledBorderStyleProperty createStyledBorderStyleProperty(StyledPropertyKey<BorderStyle> key, Node node, BorderStyle fallbackValue) {
		return createStyledProperty(key, node, new AdaptiveObjectProperty<>(fallbackValue),  (Style.StyledPropertyFactory<BorderStyle, AdaptiveObjectProperty<BorderStyle>, StyledBorderStyleProperty>)StyledBorderStyleProperty::new);
	}
	
	protected  <T, A extends AdaptiveProperty<T, ?>, P extends StyledProperty<T, ?, A, ?>> P createStyledProperty(StyledPropertyKey<T> name, Node node, A fallbackValue, Style.StyledPropertyFactory<T, A, P> factory) {
		Style<T, A> style = getStyle(name, false, fallbackValue.getClass());
		return style != null ? style.toProperty(name, node, fallbackValue, factory) : null;
	}
	
	@Contract("_, true, _ -> !null")
	@SuppressWarnings("unchecked")
	protected <T, A extends AdaptiveProperty<T, ?>> Style<T, A> getStyle(StyledPropertyKey<T> propertyName, boolean createIfMissing, Class<A> adaptiveType) {
		return (Style<T, A>)getGenericStyle(propertyName, createIfMissing);
	}
	
	@Contract("_, true -> !null")
	@SuppressWarnings("unchecked")
	protected <T> Style<T, ?> getStyle(StyledPropertyKey<T> propertyName, boolean createIfMissing) {
		return (Style<T, ?>)getGenericStyle(propertyName, createIfMissing);
	}
	
	@Contract("_, true -> !null")
	protected Style<?, ?> getGenericStyle(StyledPropertyKey<?> propertyName, boolean createIfMissing) {
		Style<?, ?> style = styles.get(propertyName);
		
		if (style == null && createIfMissing) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
	public InputStream createInputStream() {
		if (path == null) {
			return null;
		}
		return ResourceUtils.getResourceStream(path);
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void transmit(StyleSheet target) {
		target.reset();
		target.styles.putAll(styles);
	}
	
	@Override
	public void destroy() {
		AssetPools.styleSheets.remove(this);
		reset();
	}
	
	public void reset() {
		styles.clear();
	}
	
	@Override
	public @NotNull String toString() {
		return toFormattedString(false);
	}
	
	@Override
	public void print(Logger logger) {
		logger.log(toFormattedString(true));
	}
	
	public @NotNull String toFormattedString(boolean newLines) {
		String delimiter = newLines ? "\n" : " ";
		Map<String, StringJoiner> selectorToProperties = new HashMap<>();
		
		for (Map.Entry<StyledPropertyKey<?>, Style<?, ?>> styleEntry : styles.entrySet()) {
			StyledPropertyKey<?> propertyName = styleEntry.getKey();
			Style<?, ?> style = styleEntry.getValue();
			
			for (StyleRule<?> rule : style.getRules()) {
				String selector = rule.selector().toString();
				String property = String.format("%s: %s;", propertyName.toString(), rule.value().toString());
				
				if (newLines) {
					property = "\t" + property;
				}
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(delimiter))
					.add(property);
			}
			
			for (StyleRule<AnimationCurve> transitionRule : style.getTransitionRules()) {
				String selector = transitionRule.selector().toString();
				String property = String.format("transition: %s %s;", propertyName.toString(), transitionRule.value().toString());
				
				if (newLines) {
					property = "\t" + property;
				}
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(delimiter))
					.add(property);
			}
		}
		
		StringJoiner result = new StringJoiner(delimiter);
		for (Map.Entry<String, StringJoiner> entry : selectorToProperties.entrySet()) {
			result.add(entry.getKey() + " {");
			result.add(entry.getValue().toString());
			result.add("}");
		}
		
		return result.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof StyleSheet styleSheet && equals(styleSheet));
	}
	
	public boolean equals(StyleSheet styleSheet) {
		return styleSheet != null && Objects.equals(styles, styleSheet.styles);
	}
	
	@Override
	public StyleSheet self() {
		return this;
	}
	
	public HotStyleSheet toHotStyleSheet(DirectoryWatcher directoryWatcher) {
		return HotStyleSheet.fromStyleSheet(directoryWatcher, this);
	}
	
	private static StyleSheet createDefault() {
		StyleSheet styleSheet = new StyleSheet();
		
		Color buttonText = new Color(0, 0, 0);
		Color buttonFace = new Color(233, 233, 237);
		Color buttonHoverFace = new Color(208, 208, 215);
		
		styleSheet.addDefaultRule(Selector.UNIVERSAL, StyledPropertyKey.COLOR, Color.white());
		
		styleSheet.addDefaultRule(TypeSelector.P, StyledPropertyKey.MARGIN, new DualDimension(Dimension.auto(), new Dimension(1, Unit.ELEMENT_SIZE)));
		styleSheet.addDefaultRule(TypeSelector.P, StyledPropertyKey.CURSOR, CursorType.TEXT);
		
		styleSheet.addDefaultRule(TypeSelector.INPUT, StyledPropertyKey.PADDING, new DualDimension(new Dimension(1), new Dimension(4)));
		styleSheet.addDefaultRule(TypeSelector.INPUT, StyledPropertyKey.CURSOR, CursorType.TEXT);
		
		styleSheet.addDefaultRule(TypeSelector.BUTTON, StyledPropertyKey.PADDING, new DualDimension(new Dimension(1), new Dimension(4)));
		styleSheet.addDefaultRule(TypeSelector.BUTTON, StyledPropertyKey.COLOR, buttonText);
		styleSheet.addDefaultRule(TypeSelector.BUTTON, StyledPropertyKey.BACKGROUND_COLOR, buttonFace);
		styleSheet.addDefaultRule(TypeSelector.BUTTON, StyledPropertyKey.CURSOR, CursorType.POINTER);
		styleSheet.addDefaultRule( new SelectorCombo(TypeSelector.BUTTON, ModifierSelector.HOVER), StyledPropertyKey.BACKGROUND_COLOR, buttonHoverFace);
		
		return styleSheet;
	}
	
}
