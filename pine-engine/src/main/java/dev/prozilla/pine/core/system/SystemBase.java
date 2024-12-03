package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.ComponentGroup;
import dev.prozilla.pine.core.entity.Entity;

import java.util.Objects;
import java.util.function.Consumer;

// TO DO: ignore de-activated components

/**
 * System responsible for running logic for a specific type of component.
 */
public abstract class SystemBase implements Lifecycle {
	
	private final ComponentCollector collector;
	
	protected World world;
	
	public SystemBase(ComponentCollector collector) {
		Objects.requireNonNull(collector, "Collector must not be null.");
		
		this.collector = collector;
	}
	
	public void initSystem(World world) {
		Objects.requireNonNull(world, "World must not be null.");
		
		this.world = world;
	}
	
	@Override
	public void destroy() {
		collector.destroy();
	}
	
	/**
	 * Registers an entity's components in this system's collection.
	 * @see ComponentCollector
	 */
	public void register(Entity entity) {
		collector.register(entity);
	}
	
	/**
	 * Iterates over each component group in this system's collection.
	 */
	protected void forEach(Consumer<ComponentGroup> consumer) {
		for (ComponentGroup componentGroup : collector.componentGroups) {
			if (componentGroup.isEnabled()) {
				try {
					consumer.accept(componentGroup);
				} catch (Exception e) {
					System.err.println("Failed to run system " + getClass().getName());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns true if this system has collected any component groups.
	 * @see ComponentCollector
	 */
	public boolean hasComponentGroups() {
		return !collector.componentGroups.isEmpty();
	}
}
