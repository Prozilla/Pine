package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.math.MathUtils;

/**
 * A cubic Bézier easing function.
 */
public class CubicBezierEasing implements EasingFunction, Cloneable<CubicBezierEasing> {

	protected final float x1, y1, x2, y2;
	
	/**
	 * @param x1 X coordinate of first control point
	 * @param y1 Y coordinate of first control point
	 * @param x2 X coordinate of second control point
	 * @param y2 Y coordinate of second control point
	 */
	public CubicBezierEasing(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public float get(float t) {
		return MathUtils.cubicBezier(t, x1, y1, x2, y2);
	}
	
	@Override
	public boolean equals(Object other) {
		return other == this || (other instanceof CubicBezierEasing cubicBezierEasing && equals(cubicBezierEasing));
	}
	
	@Override
	public boolean equals(CubicBezierEasing cubicBezierEasing) {
		return cubicBezierEasing != null && cubicBezierEasing.x1 == x1 && cubicBezierEasing.y1 == y1
			       && cubicBezierEasing.x2 == x2 && cubicBezierEasing.y2 == y2;
	}
	
	@Override
	public CubicBezierEasing clone() {
		return new CubicBezierEasing(x1, y1, x2, y2);
	}
	
	@Override
	public String toString() {
		return String.format("cubic-bezier(%s, %s, %s, %s)", x1, y1, x2, y2);
	}
	
}
