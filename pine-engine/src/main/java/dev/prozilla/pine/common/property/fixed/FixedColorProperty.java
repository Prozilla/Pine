package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.Color;
import org.jetbrains.annotations.Contract;

public class FixedColorProperty extends FixedObjectProperty<Color> implements ColorProperty {
	
	/**
	 * Creates a fixed property with the given value.
	 */
	public FixedColorProperty(Color value) {
		super(value);
	}
	
	@Override
	public FixedColorProperty replaceNull(Color defaultValue) {
		if (isNotNull()) {
			return this;
		} else {
			return new FixedColorProperty(defaultValue);
		}
	}
	
	@Contract("-> this")
	@Override
	public FixedColorProperty snapshot() {
		return this;
	}
	
}
