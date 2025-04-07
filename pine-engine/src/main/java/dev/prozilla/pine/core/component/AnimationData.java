package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Animatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for components with animated properties.
 */
public abstract class AnimationData extends Component implements Animatable {
	
	public List<Animatable> properties;
	public final boolean applyTimeScale;
	
	public AnimationData(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
		
		properties = new ArrayList<>();
	}
	
	protected void changeProperty(Animatable oldProperty, Animatable newProperty) {
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
