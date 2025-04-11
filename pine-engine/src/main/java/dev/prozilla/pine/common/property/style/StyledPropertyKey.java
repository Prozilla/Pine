package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.ParseFunction;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;

public final class StyledPropertyKey<T> {
	
	public static final StyledPropertyKey<Color> COLOR = new StyledPropertyKey<>("color", Color::parse);
	public static final StyledPropertyKey<Color> BACKGROUND_COLOR = new StyledPropertyKey<>("background-color", Color::parse);
	public static final StyledPropertyKey<DualDimension> SIZE = new StyledPropertyKey<>("size", DualDimension::parse);
	public static final StyledPropertyKey<DualDimension> PADDING = new StyledPropertyKey<>("padding", DualDimension::parse);
	public static final StyledPropertyKey<DualDimension> POSITION = new StyledPropertyKey<>("position", DualDimension::parse);
	public static final StyledPropertyKey<GridAlignment> ANCHOR = new StyledPropertyKey<>("anchor", GridAlignment::parse);
	public static final StyledPropertyKey<Integer> GAP = new StyledPropertyKey<>("gap", Integer::valueOf);
	
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
		// TO DO: Use hashmap instead
		return switch (string) {
			case "color" -> COLOR;
			case "background-color" -> BACKGROUND_COLOR;
			case "size" -> SIZE;
			case "padding" -> PADDING;
			case "position" -> POSITION;
			case "anchor" -> ANCHOR;
			case "gap" -> GAP;
			default -> null;
		};
	}
	
}
