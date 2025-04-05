package dev.prozilla.pine.common.property.animated;

public enum AnimationDirection {
	NORMAL {
		@Override
		public float get(float time, float duration) {
			return Math.clamp(time / duration, 0, 1);
		}
	},
	REVERSE {
		@Override
		public float get(float time, float duration) {
			return Math.clamp(1 - time / duration, 0, 1);
		}
	},
	ALTERNATE {
		@Override
		public float get(float time, float duration) {
			return 1f - Math.abs((time % (2f * duration)) / duration - 1f);
		}
	};
	
	public abstract float get(float time, float duration);
}
