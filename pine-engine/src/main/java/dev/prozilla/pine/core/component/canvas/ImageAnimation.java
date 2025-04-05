package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.core.component.AnimationData;

/**
 * A component that animates an {@link ImageRenderer} component.
 */
public class ImageAnimation extends AnimationData {

	public AdaptiveColorProperty colorProperty;
	
	public ImageAnimation() {
		super( false);
		setProperties(colorProperty);
	}

}
