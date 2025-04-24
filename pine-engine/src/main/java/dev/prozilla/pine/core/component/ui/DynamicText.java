package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.property.adaptive.AdaptiveStringProperty;
import dev.prozilla.pine.core.component.Component;

public class DynamicText extends Component {

	public AdaptiveStringProperty textProperty;
	
	public DynamicText(AdaptiveStringProperty textProperty) {
		this.textProperty = textProperty;
	}
	
}
