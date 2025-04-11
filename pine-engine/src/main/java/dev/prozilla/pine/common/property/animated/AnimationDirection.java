package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.util.EnumUtils;

public enum AnimationDirection {
	NORMAL("normal") {
		@Override
		public float get(float time, float duration) {
			return Math.clamp(time / duration, 0, 1);
		}
	},
	REVERSE("reverse") {
		@Override
		public float get(float time, float duration) {
			return Math.clamp(1 - time / duration, 0, 1);
		}
	},
	ALTERNATE("alternate") {
		@Override
		public float get(float time, float duration) {
			return 1f - Math.abs((time % (2f * duration)) / duration - 1f);
		}
	};
	
	private final String string;
	
	AnimationDirection(String string) {
		this.string = string;
	}
	
	public abstract float get(float time, float duration);
	
	@Override
	public String toString() {
		return string;
	}
	
	public static AnimationDirection parse(String input) {
		return EnumUtils.findByName(AnimationDirection.values(), input);
	}
	
}
