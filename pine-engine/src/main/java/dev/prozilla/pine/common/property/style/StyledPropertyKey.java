package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.ParseFunction;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.component.ui.LayoutNode;

public final class StyledPropertyKey<T> {
	
	public static final StyledPropertyKey<Color> COLOR = new StyledPropertyKey<>("color", Color::parse);
	public static final StyledPropertyKey<Color> BACKGROUND_COLOR = new StyledPropertyKey<>("background-color", Color::parse);
	public static final StyledPropertyKey<DualDimension> SIZE = new StyledPropertyKey<>("size", DualDimension::parse);
	public static final StyledPropertyKey<DualDimension> PADDING = new StyledPropertyKey<>("padding", DualDimension::parse);
	public static final StyledPropertyKey<DualDimension> POSITION = new StyledPropertyKey<>("position", DualDimension::parse);
	public static final StyledPropertyKey<GridAlignment> ANCHOR = new StyledPropertyKey<>("anchor", GridAlignment::parse);
	public static final StyledPropertyKey<DimensionBase> GAP = new StyledPropertyKey<>("gap", Dimension::parse);
	public static final StyledPropertyKey<Direction> DIRECTION = new StyledPropertyKey<>("flex-direction", (String input) -> switch (input) {
		case "row" -> Direction.RIGHT;
		case "row-reverse" -> Direction.LEFT;
		case "column" -> Direction.DOWN;
		case "column-reverse" -> Direction.UP;
		default -> Direction.parse(input);
	});
	public static final StyledPropertyKey<EdgeAlignment> ALIGNMENT = new StyledPropertyKey<>("align-items", (String input) -> switch (input) {
		case "flex-start" -> EdgeAlignment.START;
		case "center" -> EdgeAlignment.CENTER;
		case "flex-end" -> EdgeAlignment.END;
		default -> EdgeAlignment.parse(input);
	});
	public static final StyledPropertyKey<LayoutNode.Distribution> DISTRIBUTION = new StyledPropertyKey<>("justify-content", (String input) -> switch (input) {
		case "flex-start" -> LayoutNode.Distribution.START;
		case "center" -> LayoutNode.Distribution.CENTER;
		case "flex-end" -> LayoutNode.Distribution.END;
		case "space-between" -> LayoutNode.Distribution.SPACE_BETWEEN;
		default -> LayoutNode.Distribution.parse(input);
	});
	
	private static final StyledPropertyKey<?>[] keys = new StyledPropertyKey<?>[] {
		COLOR,
		BACKGROUND_COLOR,
		SIZE,
		PADDING,
		POSITION,
		ANCHOR,
		GAP,
		DIRECTION,
		ALIGNMENT,
		DISTRIBUTION,
	};
	
	private final String string;
	private final ParseFunction<T> valueParser;
	
	private StyledPropertyKey(String string, ParseFunction<T> valueParser) {
		this.string = string;
		this.valueParser = valueParser;
	}
	
	public String toString() {
		return string;
	}
	
	public T parseValue(String input) {
		return valueParser.parse(input);
	}
	
	public static StyledPropertyKey<?> parse(String string) {
		return ArrayUtils.findByString(keys, string);
	}
	
}
