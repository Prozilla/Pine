package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.Animatable;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.animated.AnimatedObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimatedPropertyBase;

/**
 * Base class for an optimized property that can either have a fixed value or be dynamically changed by a {@link Property}.
 *
 * <p>
 *     This class provides a more efficient alternative to using {@link Property} exclusively,
 *     allowing for fixed values when dynamic behavior is unnecessary.
 * </p>
 * <ul>
 *     <li>If a {@link Property} is provided, the property updates dynamically.</li>
 *     <li>If an {@link AnimatedObjectProperty} is provided, the property will be updated each frame by the animation.</li>
 *     <li>If neither is provided, the property remains fixed.</li>
 * </ul>
 * <p>
 *     Certain implementations store fixed values as primitives to optimize performance and avoid unnecessary (un)boxing.
 * </p>
 */
public abstract class AdaptiveProperty<T, P extends Property<T>> implements Property<T>, Animatable {

	protected final P property;
	protected final AnimatedPropertyBase<?> animatedProperty;
	
	/**
	 * Creates a new property with a dynamic value if {@code variableProperty} is not {@code null}, or a fixed value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveProperty(P property) {
		this.property = property;
		
		if (property instanceof AnimatedPropertyBase<?> animatedProperty) {
			this.animatedProperty = animatedProperty;
		} else {
			this.animatedProperty = null;
		}
	}
	
	/**
	 * Restarts the animation of this property, if it is animated.
	 */
	@Override
	public void restartAnimation() {
		if (animatedProperty != null) {
			animatedProperty.restart();
		}
	}
	
	/**
	 * Updates the animation of this property, if it is animated.
	 */
	@Override
	public void updateAnimation(float deltaTime) {
		if (animatedProperty != null) {
			animatedProperty.update(deltaTime);
		}
	}
	
	/**
	 * Checks whether this property has a dynamic value.
	 */
	public boolean isDynamic() {
		return property != null;
	}
	
	/**
	 * Checks whether this property has an animated value.
	 */
	public boolean isAnimated() {
		return animatedProperty != null;
	}
	
}
