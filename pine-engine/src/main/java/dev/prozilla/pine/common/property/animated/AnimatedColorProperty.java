package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.Color;

public class AnimatedColorProperty extends AnimatedObjectProperty<Color> implements ColorProperty {
	
	/**
	 * Creates a color property with an animation.
	 * @param start The color at the start of the animation
	 * @param end The color at the end of the animation
	 * @param curve The animation curve that determines how this animation progresses over time
	 */
	public AnimatedColorProperty(Color start, Color end, AnimationCurve curve) {
		super(start, end, curve);
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	/**
	 * Updates the value of this property and transmits it to a given color.
	 * @param deltaTime The delta time, in seconds
	 * @param outputColor The target color
	 */
	public void applyUpdatedValue(float deltaTime, Color outputColor) {
		update(deltaTime);
		transmit(outputColor);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(start);
		target.mix(end, getProgress());
	}
	
	/**
	 * A color property with an animated alpha value.
	 */
	public static class AnimatedAlpha extends AnimatedObjectProperty<Color> implements ColorProperty {
		
		protected final float alphaStart;
		protected final float alphaEnd;
		
		/**
		 * Creates a color property with an animated alpha value.
		 * @param color The base color
		 * @param alphaStart The value at the start of the animation
		 * @param alphaEnd The value at the end of the animation
		 * @param curve The animation curve that determines how this animation progresses over time
		 */
		public AnimatedAlpha(Color color, float alphaStart, float alphaEnd, AnimationCurve curve) {
			super(color, null, curve);
			this.alphaStart = alphaStart;
			this.alphaEnd = alphaEnd;
		}
		
		@Override
		public Color getValue() {
			return getColor();
		}
		
		@Override
		public void transmit(Color target) {
			target.receive(start);
			target.setAlpha(MathUtils.remap(getProgress(), 0f, 1f, alphaStart, alphaEnd));
		}
		
	}
	
}
