package dev.prozilla.pine.common.math;

public final class MathUtils {
	
	/**
	 * Clamps a value between a min and a max value.
	 * @param value Value
	 * @param min Min value
	 * @param max Max value
	 * @return Clamped value
	 */
	public static float clamp(float value, float min, float max) {
		return Math.max(Math.min(value, max), min);
	}
	
	/**
	 * Clamps a value between a min and a max value.
	 * @param value Value
	 * @param min Min value
	 * @param max Max value
	 * @return Clamped value
	 */
	public static int clamp(int value, int min, int max) {
		return Math.max(Math.min(value, max), min);
	}
	
	/**
	 * Linear interpolation between two values.
	 * @param start Start value
	 * @param end End value
	 * @param step Amount to interpolate (between 0f and 1f)
	 * @return Interpolated value between start and value
	 */
	public static float lerp(float start, float end, float step) {
		return (start * (1.0f - step)) + (end * step);
	}
	
	/**
	 * Remaps a float value from one range to another.
	 * @param value The input float value to remap
	 * @param x The lower bound of the original range
	 * @param y The upper bound of the original range
	 * @param a The lower bound of the target range
	 * @param b The upper bound of the target range
	 * @return The remapped value in the range [a, b]
	 */
	public static float remap(float value, float x, float y, float a, float b) throws IllegalArgumentException {
		if (x == y) {
			throw new IllegalArgumentException("Original range cannot have the same lower and upper bounds.");
		}
		float normalized = (value - x) / (y - x);
		return a + normalized * (b - a);
	}
	
	public static float easeOutQuad(float time) {
		return 1f - (1f - time) * (1f - time);
	}
}
