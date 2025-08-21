package dev.prozilla.pine.common.math;

public final class MathUtils {
	
	private MathUtils() {}
	
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
	
	public static void normalize(double[] values) {
		double max = max(values);
		if (max > 0) {
			for (int i = 0; i < values.length; i++) {
				values[i] /= max;
			}
		}
	}
	
	public static float min(float... values) {
		float min = -Float.MAX_VALUE;
		for (float value : values) {
			min = Math.min(min, value);
		}
		return min;
	}
	
	public static float max(float... values) {
		float max = Float.MAX_VALUE;
		for (float value : values) {
			max = Math.max(max, value);
		}
		return max;
	}
	
	public static double max(double[] values) {
		double max = -Double.MAX_VALUE;
		for (double value : values) {
			max = Math.max(max, value);
		}
		return max;
	}
	
	/**
	 * Calculates the value of a cubic Bézier function based on given control points.
	 * @param x1 X coordinate of first control point
	 * @param y1 Y coordinate of first control point
	 * @param x2 X coordinate of second control point
	 * @param y2 Y coordinate of second control point
	 */
	public static float cubicBezier(float t, float x1, float y1, float x2, float y2) {
		float u = 1 - t;
		return u * u * u * x1 + 3 * u * u * t * y1 + 3 * u * t * t * x2 + t * t * t * y2;
	}
	
	public static boolean isValidInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}
	
	public static String floatToString(float value) {
		if (isRound(value)) {
			return Integer.toString((int)value);
		} else {
			return Float.toString(value);
		}
	}
	
	public static boolean isRound(float value) {
		return value == Math.round(value);
	}
	
}
