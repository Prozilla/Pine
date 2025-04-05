package dev.prozilla.pine.common.property.animated;

public enum AnimationDirection {
	NORMAL {
		@Override
		float get(float time, float duration) {
			return Math.clamp(time / duration, 0, 1);
		}
	},
	REVERSE {
		@Override
		float get(float time, float duration) {
			return Math.clamp(1 - time / duration, 0, 1);
		}
	},
	ALTERNATE {
		@Override
		float get(float time, float duration) {
			return 1f - Math.abs((time % (2f * duration)) / duration - 1f);
		}
	};
	
	abstract float get(float time, float duration);
}
