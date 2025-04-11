package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.util.ArrayUtils;

/**
 * 1-dimensional anchor point relative to the left side of a line with length <code>1f</code>.
 */
public enum EdgeAlignment {
	START(0, "start"),
	CENTER(0.5f, "center"),
	END(1, "end");
	
	public final float factor;
	
	private final String string;
	
	EdgeAlignment(float factor, String string) {
		this.factor = factor;
		this.string = string;
	}
	
	/**
	 * Calculates the position based a given size.
	 */
	public float getValue(float totalSize) {
		return totalSize * factor;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
	public static EdgeAlignment parse(String input) {
		return ArrayUtils.findByString(EdgeAlignment.values(), input);
	}
	
}
