package dev.prozilla.pine.core.component.animation;

import dev.prozilla.pine.common.Animatable;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A component that handles animated properties of other components.
 */
public class AnimationData extends Component implements Animatable {
	
	public final List<Animatable> properties;
	public final boolean applyTimeScale;
	
	public AnimationData(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
		
		properties = new ArrayList<>();
	}
	
	public void replaceProperty(Animatable oldProperty, Animatable newProperty) {
		if (oldProperty != null) {
			properties.remove(oldProperty);
		}
		if (newProperty != null) {
			properties.add(newProperty);
		}
	}
	
	@Override
	public void restartAnimation() {
		for (Animatable property : properties) {
			property.restartAnimation();
		}
	}
	
	@Override
	public void updateAnimation(float deltaTime) {
		for (Animatable property : properties) {
			property.updateAnimation(deltaTime);
		}
	}
	
}
