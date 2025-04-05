package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AnimatedColorProperty extends AnimatedProperty<Color> implements ColorProperty {
	
	public AnimatedColorProperty(Color start, Color end, AnimationCurve curve) {
		super(start, end, curve);
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	public void applyUpdatedValue(float deltaTime, Color outputColor) {
		update(deltaTime);
		transmit(outputColor);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(start);
		target.mix(end, getFactor());
	}
	
	/**
	 * A color property with an animated alpha value.
	 */
	public static class AnimatedAlpha extends AnimatedProperty<Color> implements ColorProperty {
		
		protected final float alphaStart;
		protected final float alphaEnd;
		
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
			target.setAlpha(MathUtils.remap(getFactor(), 0f, 1f, alphaStart, alphaEnd));
		}
	}
}
