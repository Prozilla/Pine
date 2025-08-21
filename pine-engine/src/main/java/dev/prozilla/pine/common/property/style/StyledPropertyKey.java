package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.DualDimensionParser;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.EnumParser;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.core.component.ui.LayoutNode;

import java.util.Objects;

public final class StyledPropertyKey<T> {
	
	private static final ColorParser colorParser = new ColorParser();
	private static final DualDimensionParser dualDimensionParser = new DualDimensionParser();
	
	public static final StyledPropertyKey<Color> COLOR = new StyledPropertyKey<>("color", colorParser);
	public static final StyledPropertyKey<Color> BACKGROUND_COLOR = new StyledPropertyKey<>("background-color", colorParser);
	public static final StyledPropertyKey<DualDimension> SIZE = new StyledPropertyKey<>("size", dualDimensionParser);
	public static final StyledPropertyKey<DualDimension> PADDING = new StyledPropertyKey<>("padding", dualDimensionParser);
	public static final StyledPropertyKey<DualDimension> MARGIN = new StyledPropertyKey<>("margin", dualDimensionParser);
	public static final StyledPropertyKey<GridAlignment> ANCHOR = new StyledPropertyKey<>("anchor", new EnumParser<>(GridAlignment.values()));
	public static final StyledPropertyKey<DimensionBase> GAP = new StyledPropertyKey<>("gap", new DimensionParser());
	public static final StyledPropertyKey<Direction> DIRECTION = new StyledPropertyKey<>("flex-direction", new Parser<>() {
		@Override
		public boolean parse(String input) {
			Direction direction = switch (input) {
				case "row" -> Direction.RIGHT;
				case "row-reverse" -> Direction.LEFT;
				case "column" -> Direction.DOWN;
				case "column-reverse" -> Direction.UP;
				default -> Direction.parse(input);
			};
			
			if (direction == null) {
				return fail();
			}
			
			return succeed(direction);
		}
	});
	public static final StyledPropertyKey<EdgeAlignment> ALIGNMENT = new StyledPropertyKey<>("align-items", new Parser<>() {
		@Override
		public boolean parse(String input) {
			EdgeAlignment edgeAlignment = switch (input) {
				case "flex-start" -> EdgeAlignment.START;
				case "center" -> EdgeAlignment.CENTER;
				case "flex-end" -> EdgeAlignment.END;
				default -> EdgeAlignment.parse(input);
			};
			
			if (edgeAlignment == null) {
				return fail();
			}
			
			return succeed(edgeAlignment);
		}
	});
	public static final StyledPropertyKey<LayoutNode.Distribution> DISTRIBUTION = new StyledPropertyKey<>("justify-content", new Parser<>() {
		@Override
		public boolean parse(String input) {
			LayoutNode.Distribution distribution = switch (input) {
				case "flex-start" -> LayoutNode.Distribution.START;
				case "center" -> LayoutNode.Distribution.CENTER;
				case "flex-end" -> LayoutNode.Distribution.END;
				case "space-between" -> LayoutNode.Distribution.SPACE_BETWEEN;
				default -> LayoutNode.Distribution.parse(input);
			};
			
			if (distribution == null) {
				return fail();
			}
			
			return succeed(distribution);
		}
	});
	
	private static final StyledPropertyKey<?>[] keys = new StyledPropertyKey<?>[] {
		COLOR,
		BACKGROUND_COLOR,
		SIZE,
		PADDING,
		MARGIN,
		ANCHOR,
		GAP,
		DIRECTION,
		ALIGNMENT,
		DISTRIBUTION,
	};
	
	private final String string;
	private final Parser<T> valueParser;
	
	private StyledPropertyKey(String string, Parser<T> valueParser) {
		this.string = string;
		this.valueParser = valueParser;
	}
	
	public String toString() {
		return string;
	}
	
	public T parseValue(String input) {
		return valueParser.read(input);
	}
	
	public static StyledPropertyKey<?> parse(String string) {
		return ArrayUtils.findByString(keys, string, true);
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof StyledPropertyKey<?> styledPropertyKey && equals(styledPropertyKey));
	}
	
	public boolean equals(StyledPropertyKey<?> styledPropertyKey) {
		return styledPropertyKey != null && Objects.equals(string, styledPropertyKey.string);
	}
	
}
