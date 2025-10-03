package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.Color;

public class FixedColorProperty extends FixedObjectProperty<Color> implements ColorProperty {
	
	public FixedColorProperty(Color value) {
		super(value);
	}
	
}
