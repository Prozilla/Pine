package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.math.MathUtils;

/**
 * A cubic Bézier easing function.
 */
public class CubicBezierEasing implements EasingFunction {

	protected final float p0, p1, p2, p3;
	
	/**
	 * @param p0 X coordinate of first control point
	 * @param p1 Y coordinate of first control point
	 * @param p2 X coordinate of second control point
	 * @param p3 Y coordinate of second control point
	 */
	public CubicBezierEasing(float p0, float p1, float p2, float p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	@Override
	public float get(float t) {
		return MathUtils.cubicBezier(t, p0, p1, p2, p3);
	}
	
	@Override
	public String toString() {
		return String.format("cubic-bezier(%s, %s, %s, %s)", p0, p1, p2, p3);
	}
	
}
