package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Animatable;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimationData extends Component {
	
	public List<Animatable> properties;
	public final boolean applyTimeScale;
	
	public AnimationData( boolean applyTimeScale) {
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
	
	public void restartAnimation() {
		for (Animatable property : properties) {
			property.restartAnimation();
		}
	}
	
	public void updateAnimation(float deltaTime) {
		for (Animatable property : properties) {
			property.updateAnimation(deltaTime);
		}
	}
	
}
