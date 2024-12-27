package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.array.ArrayUtils;

/**
 * Units used in dimensions of UI elements.
 * @see Dimension
 */
public enum Unit {
	
	/**
	 * Automatic size, irrespective of dimension value.
	 */
	AUTO,
	
	/**
	 * Absolute size in pixels, equivalent to <code>px</code> in CSS.
	 */
	PIXELS,
	
	/**
	 * Relative to size of the element, equivalent to <code>vh</code> in CSS.
	 */
	ELEMENT_SIZE,
	
	/**
	 * Relative to viewport width, equivalent to <code>vh</code> in CSS.
	 * Value <code>100</code> corresponds to the full width of the viewport.
	 */
	VIEWPORT_WIDTH,
	
	/**
	 * Relative to viewport height, equivalent to <code>vh</code> in CSS.
	 * Value <code>100</code> corresponds to the full height of the viewport.
	 */
	VIEWPORT_HEIGHT;
	
	private static final Unit[] DYNAMIC_UNITS = new Unit[]{
		Unit.VIEWPORT_WIDTH,
	    Unit.VIEWPORT_HEIGHT,
	};
	
	public static boolean isFixed(Unit unit) {
		return !isDynamic(unit);
	}
	
	public static boolean isDynamic(Unit unit) {
		return ArrayUtils.contains(DYNAMIC_UNITS, unit);
	}
	
	public static String toString(Unit unit) {
		return switch (unit) {
			case AUTO -> "auto";
			case PIXELS -> "px";
			case ELEMENT_SIZE -> "em";
			case VIEWPORT_WIDTH -> "vw";
			case VIEWPORT_HEIGHT -> "vh";
		};
	}
}
