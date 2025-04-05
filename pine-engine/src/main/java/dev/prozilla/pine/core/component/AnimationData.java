package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimationData extends Component {
	
	public List<AdaptivePropertyBase<?>> properties;
	public final boolean applyTimeScale;
	
	public AnimationData( boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
		
		properties = new ArrayList<>();
	}
	
	protected void changeProperty(AdaptivePropertyBase<?> oldProperty, AdaptivePropertyBase<?> newProperty) {
		if (oldProperty != null) {
			properties.remove(oldProperty);
		}
		if (newProperty != null) {
			properties.add(newProperty);
		}
	}
	
	public void restartAnimation() {
		for (AdaptivePropertyBase<?> property : properties) {
			property.restartAnimation();
		}
	}
	
	public void updateAnimation(float deltaTime) {
		for (AdaptivePropertyBase<?> property : properties) {
			property.updateAnimation(deltaTime);
		}
	}
	
}
