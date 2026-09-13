package dev.prozilla.pine.core.component.driver;

import dev.prozilla.pine.common.Animatable;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.animation.AnimationData;

public class Driver extends Component {
	
	protected final AnimationData animationData;
	
	public Driver(AnimationData animationData) {
		this.animationData = animationData;
	}
	
	public <A extends Animatable> A replaceProperty(A oldProperty, A newProperty) {
		return animationData.replaceProperty(oldProperty, newProperty);
	}
}
