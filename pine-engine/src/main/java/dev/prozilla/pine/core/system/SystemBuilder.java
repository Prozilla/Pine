package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityQuery;

public abstract class SystemBuilder<S extends SystemBase, B extends SystemBuilder<S, B>> {
	
	protected final Class<? extends Component>[] componentTypes;
	protected String entityTag;
	
	public SystemBuilder(Class<? extends Component>[] componentTypes) {
		this.componentTypes = componentTypes;
	}
	
	/**
	 * Restricts this system's query to entities with a given tag.
	 * @see EntityQuery
	 */
	public B setRequiredTag(String entityTag) {
		this.entityTag = entityTag;
		return self();
	}
	
	/**
	 * Ensures subclasses return the correct type in method chains.
	 */
	protected abstract B self();
	
	/**
	 * Creates a new system.
	 */
	public abstract S build();
	
	protected S finishBuild(S system) {
		if (entityTag != null) {
			system.setRequiredTag(entityTag);
		}
		
		return system;
	}
}
