package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.animated.AnimatedProperty;

/**
 * Base class for an optimized property that can either have a fixed value or be dynamically changed by a {@link VariableProperty}.
 *
 * <p>
 *     This class provides a more efficient alternative to using {@link VariableProperty} exclusively,
 *     allowing for fixed values when dynamic behavior is unnecessary.
 * </p>
 * <ul>
 *     <li>If a {@link VariableProperty} is provided, the property updates dynamically.</li>
 *     <li>If an {@link AnimatedProperty} is provided, the property will be updated each frame by the animation.</li>
 *     <li>If neither is provided, the property remains fixed.</li>
 * </ul>
 * <p>
 *     Certain implementations store fixed values as primitives to optimize performance and avoid unnecessary (un)boxing.
 * </p>
 */
public abstract class AdaptivePropertyBase<T> extends VariableProperty<T> {

	protected final VariableProperty<T> variableProperty;
	private final AnimatedProperty<T> animatedProperty;
	
	/**
	 * Creates a new property with a dynamic value if {@code variableProperty} is not {@code null}, or a fixed value.
	 * @param variableProperty Variable property that determines the value of this property
	 */
	public AdaptivePropertyBase(VariableProperty<T> variableProperty) {
		this.variableProperty = variableProperty;
		
		if (variableProperty instanceof AnimatedProperty<T> animatedProperty) {
			this.animatedProperty = animatedProperty;
		} else {
			this.animatedProperty = null;
		}
	}
	
	/**
	 * Restarts the animation of this property, if it is animated.
	 */
	public void restartAnimation() {
		if (animatedProperty != null) {
			animatedProperty.restart();
		}
	}
	
	/**
	 * Updates the animation of this property, if it is animated.
	 */
	public void updateAnimation(float deltaTime) {
		if (animatedProperty != null) {
			animatedProperty.update(deltaTime);
		}
	}
	
	/**
	 * Checks whether this property has a dynamic value.
	 */
	public boolean isDynamic() {
		return variableProperty != null;
	}
	
	/**
	 * Checks whether this property has an animated value.
	 */
	public boolean isAnimated() {
		return animatedProperty != null;
	}
}
