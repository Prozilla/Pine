package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.system.SystemBase;

/**
 * Base class for systems responsible for handling entity data initialization.
 */
public abstract class InitSystemBase extends SystemBase {
	
	@SafeVarargs
	public InitSystemBase(Class<? extends Component>... componentTypes) {
		super(componentTypes, true);
	}
	
	/**
	 * Initializes each entity's data.
	 */
	public abstract void init();
}
