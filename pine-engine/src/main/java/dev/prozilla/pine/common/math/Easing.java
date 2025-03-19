package dev.prozilla.pine.common.math;

/**
 * A set of easing functions.
 * Some of these functions are based on <a href="https://easings.net/">easings.net</a>.
 */
public enum Easing {
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
	};
	
	/**
	 * Applies this easing function to an input value between <code>0f</code> and <code>1f</code> and maps the output to a value between <code>start</code> and <code>end</code>.
	 * @param t Normalized input value (<code>0f</code> to <code>1f</code>)
	 * @return The eased and remapped output value.
	 */
	public float get(float t, float start, float end) {
		return MathUtils.remap(get(t), 0f, 1f, start, end);
	}
	
	/**
	 * Applies this easing function to an input value between <code>0f</code> and <code>1f</code>.
	 * Values outside of this range may produce unpredictable results.
	 * @param t Normalized input value (<code>0f</code> to <code>1f</code>)
	 * @return The eased output value.
	 */
	public abstract float get(float t);
}
