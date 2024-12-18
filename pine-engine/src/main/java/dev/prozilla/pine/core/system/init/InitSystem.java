package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;

/**
 * System for initializing entity data.
 */
public abstract class InitSystem extends InitSystemBase {
	
	@SafeVarargs
	public InitSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	public final void init() {
		forEach(this::process);
	}
	
	/**
	 * Initializes a single entity's data.
	 * @param chunk Entity that matches this system's query
	 */
	protected abstract void process(EntityChunk chunk);
}
