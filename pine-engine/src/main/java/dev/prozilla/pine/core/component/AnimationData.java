package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;

public abstract class AnimationData extends Component {
	
	public AdaptivePropertyBase<?>[] properties;
	public final boolean applyTimeScale;
	
	public AnimationData( boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
	}
	
	protected void setProperties(AdaptivePropertyBase<?>... properties) {
		this.properties = properties;
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
