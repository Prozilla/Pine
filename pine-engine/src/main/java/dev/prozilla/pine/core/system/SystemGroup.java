package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A wrapper for systems of the same type.
 */
public class SystemGroup<S extends SystemBase> {
	
	private final List<S> systems;
	private final Class<S> type;
	
	public SystemGroup(Class<S> type) {
		this.type = type;
		
		systems = new ArrayList<>();
	}
	
	/**
	 * Adds a system to this group if it matches the type of this group.
	 * @return True if the system matched the type of this group.
	 */
	public boolean add(SystemBase system) throws IllegalStateException {
		boolean valid = type.isInstance(system);
		if (valid) {
			S newSystem = type.cast(system);
			if (systems.contains(newSystem)) {
				throw new IllegalStateException("System has already been added.");
			}
			systems.add(newSystem);
		}
		return valid;
	}
	
	/**
	 * Returns true if this group is empty.
	 */
	public boolean isEmpty() {
		return systems.isEmpty();
	}
	
	public int size() {
		return systems.size();
	}
	
	/**
	 * Iterates over each system in this group.
	 */
	public void forEach(Consumer<S> consumer) {
		if (systems.isEmpty()) {
			return;
		}
		
		for (S system : systems) {
			try {
				if (system.hasEntityChunks()) {
					consumer.accept(system);
				}
			} catch (RuntimeException e) {
				System.err.println("Failed to run system: " + system.getClass().getSimpleName());
				e.printStackTrace();
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
	 * Unregisters an entity from all systems in this group.
	 */
	public void unregister(Entity entity) {
		if (systems.isEmpty()) {
			return;
		}
		
		for (SystemBase system : systems) {
			system.unregister(entity);
		}
	}
	
	/**
	 * Removes all systems from this group.
	 */
	public void clear() {
		systems.clear();
	}
}
