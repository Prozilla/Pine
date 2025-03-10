package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.Container;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * A wrapper for systems of the same type.
 * All systems in a group are executed during the same step of the game loop.
 * @param <S> Type of the systems in this group.
 */
public class SystemGroup<S extends SystemBase> implements Container<S> {
	
	private final List<S> systems;
	/** Type of the systems in this group. */
	private final Class<S> type;
	
	private final World world;
	private final Logger logger;
	
	/**
	 * Creates a new system group in a world with systems of a given type.
	 * @param type Type of the systems in this group
	 */
	public SystemGroup(World world, Class<S> type) {
		this.world = world;
		this.type = type;
		
		logger = world.application.getLogger();
		
		systems = new ArrayList<>();
	}
	
	/**
	 * Adds a system to this group if it matches the type of this group.
	 * @return True if the system matched the type of this group.
	 */
	@Override
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
	@Override
	public boolean isEmpty() {
		return systems.isEmpty();
	}
	
	/**
	 * Returns the amount of systems in this group.
	 */
	@Override
	public int size() {
		return systems.size();
	}
	
	/**
	 * Iterates over each system in this group.
	 */
	@Override
	public void forEach(Consumer<? super S> consumer) {
		if (systems.isEmpty()) {
			return;
		}
		
		for (S system : systems) {
			try {
				if (system.shouldRun()) {
					consumer.accept(system);
				}
				
				// Abort if the world was unloaded
				if (!world.isActive()) {
					break;
				}
			} catch (RuntimeException e) {
				logger.error("Failed to run system: " + system.getClass().getSimpleName(), e);
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
	
	@Override
	public Iterator<S> iterator() {
		return systems.iterator();
	}
	
	/**
	 * Removes all systems from this group.
	 */
	public void clear() {
		systems.clear();
	}
	
}
