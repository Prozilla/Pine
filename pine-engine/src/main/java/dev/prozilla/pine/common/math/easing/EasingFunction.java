package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.math.MathUtils;

public interface EasingFunction {
	
	/**
	 * Applies this easing function to an input value between <code>0f</code> and <code>1f</code> and maps the output to a value between <code>start</code> and <code>end</code>.
	 * @param t Normalized input value (<code>0f</code> to <code>1f</code>)
	 * @return The eased and remapped output value.
	 */
	default float get(float t, float start, float end) {
		return MathUtils.remap(get(t), 0f, 1f, start, end);
	}
	
	/**
	 * Applies this easing function to an input value between <code>0f</code> and <code>1f</code>.
	 * Values outside of this range may produce unpredictable results.
	 * @param t Normalized input value (<code>0f</code> to <code>1f</code>)
	 * @return The eased output value.
	 */
	float get(float t);
	
}
