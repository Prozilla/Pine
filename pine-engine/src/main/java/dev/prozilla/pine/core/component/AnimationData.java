package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;

public abstract class AnimationData extends Component {
	
	public AdaptivePropertyBase<?>[] properties;
	
	public AnimationData(AdaptivePropertyBase<?>[] properties) {
		this.properties = properties;
	}
	
}
