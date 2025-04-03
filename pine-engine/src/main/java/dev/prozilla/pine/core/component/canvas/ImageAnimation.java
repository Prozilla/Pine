package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.core.component.Component;

public class ImageAnimation extends Component {

	public AdaptiveColorProperty colorProperty;
	
	public void restartAnimation() {
		colorProperty.restartAnimation();
	}
	
	public void updateAnimation(float deltaTime) {
		colorProperty.updateAnimation(deltaTime);
	}

}
