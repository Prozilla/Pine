package dev.prozilla.pine.common.math.vector;

/**
 * 1-dimensional anchor point relative to the left side of a line with length <code>1f</code>.
 */
public enum EdgeAlignment {
	START(0),
	CENTER(0.5f),
	END(1);
	
	public final float factor;
	
	EdgeAlignment(float factor) {
		this.factor = factor;
	}
	
	/**
	 * Calculates the position based a given size.
	 */
	public float getValue(float totalSize) {
		return totalSize * factor;
	}
}
