package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A wrapper for systems of the same type.
 */
public class SystemGroup<S extends SystemBase> {
	
	private final ArrayList<S> systems;
	private final Class<S> type;
	
	public SystemGroup(Class<S> type) {
		this.type = type;
		
		systems = new ArrayList<>();
	}
	
	/**
	 * Adds a system to this group if it matches the type of this group.
	 * @return True if the system matched the type of this group.
	 */
	public boolean add(SystemBase system) {
		boolean valid = type.isInstance(system);
		if (valid) {
			systems.add(type.cast(system));
		}
		return valid;
	}
	
	/**
	 * Returns true if this group is empty.
	 */
	public boolean isEmpty() {
		return systems.isEmpty();
	}
	
	/**
	 * Iterates over each system in this group.
	 */
	public void forEach(Consumer<S> consumer) {
		if (systems.isEmpty()) {
			return;
		}
		
		for (S system : systems) {
			if (system.hasComponentGroups()) {
				consumer.accept(system);
			}
		}
	}
	
	/**
	 * Registers an entity in all systems in this group.
	 */
	public void register(Entity entity) {
		if (systems.isEmpty()) {
			return;
		}
		
		for (SystemBase system : systems) {
			system.register(entity);
		}
	}
	
	/**
	 * Removes all systems from this group.
	 */
	public void clear() {
		systems.clear();
	}
}
