package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.math.MathUtils;

/**
 * A set of predefined easing functions.
 * Some of these functions are based on <a href="https://easings.net/">easings.net</a> and <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/easing-function">CSS easing functions</a>.
 */
public enum Easing implements EasingFunction {
	LINEAR {
		@Override
		public float get(float t) {
			return t;
		}
	},
	CLOSEST {
		@Override
		public float get(float t) {
			if (t < 0.5f) {
				return 0;
			} else {
				return 1;
			}
		}
	},
	
	// Cubic Bézier
	// Based on https://developer.mozilla.org/en-US/docs/Web/CSS/easing-function#cubic-bezier-easing-function
	EASE {
		@Override
		public float get(float t) {
			return MathUtils.cubicBezier(t, 0.25f, 0.1f, 0.25f, 1f);
		}
	},
	EASE_IN {
		@Override
		public float get(float t) {
			return MathUtils.cubicBezier(t, 0.42f, 0f, 1f, 1f);
		}
	},
	EASE_OUT {
		@Override
		public float get(float t) {
			return MathUtils.cubicBezier(t, 0f, 0f, 0.58f, 1f);
		}
	},
	EASE_IN_OUT {
		@Override
		public float get(float t) {
			return MathUtils.cubicBezier(t, 0.42f, 0f, 0.58f, 1f);
		}
	},
	
	// Sine
	EASE_IN_SINE {
		@Override
		public float get(float t) {
			return 1f - (float)Math.cos((t * Math.PI) / 2f);
		}
	},
	EASE_OUT_SINE {
		@Override
		public float get(float t) {
			return (float)Math.sin((t * Math.PI) / 2f);
		}
	},
	EASE_IN_OUT_SINE {
		@Override
		public float get(float t) {
			return -((float)Math.cos(Math.PI * t) - 1f) / 2f;
		}
	},
	
	// Quadratic
	EASE_IN_QUAD {
		@Override
		public float get(float t) {
			return t * t;
		}
	},
	EASE_OUT_QUAD {
		@Override
		public float get(float t) {
			return 1f - (1f - t) * (1f - t);
		}
	},
	EASE_IN_OUT__QUAD {
		@Override
		public float get(float t) {
			return t < 0.5f ? 2 * t * t : 1f - (float)Math.pow(-2f * t + 2f, 2f) / 2f;
		}
	}
}
