package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.core.component.Component;

public class DynamicImage extends Component {

	public AdaptiveObjectProperty<TextureAsset> imageProperty;
	
	public DynamicImage(AdaptiveObjectProperty<TextureAsset> imageProperty) {
		this.imageProperty = imageProperty;
	}
	
}
