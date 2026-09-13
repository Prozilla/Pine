package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;

/**
 * System that does nothing.
 *
 * <p>
 *     Can be used to collect {@link EntityChunk}s without having to do something with them each frame.
 * </p>
 */
public class NoOpUpdateSystem extends UpdateSystemBase {
	
	@SafeVarargs
	public NoOpUpdateSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Does nothing.
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void update(float deltaTime) {
	
	}
	
	/**
	 * @return {@code false}
	 */
	@Override
	public boolean shouldRun() {
		return false;
	}
	
}
