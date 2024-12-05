package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.ComponentGroup;
import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

// TO DO: ignore de-activated components

/**
 * System responsible for running logic for a specific type of component.
 */
public abstract class SystemBase implements Lifecycle {
	
	private final ComponentCollector collector;
	private final boolean runOnce;
	
	private final ArrayList<Integer> processedEntityIds;
	
	protected World world;
	
	public SystemBase(ComponentCollector collector) {
		this(collector, false);
	}
	
	public SystemBase(ComponentCollector collector, boolean runOnce) {
		Objects.requireNonNull(collector, "Collector must not be null.");
		
		this.collector = collector;
		this.runOnce = runOnce;
		
		processedEntityIds = new ArrayList<>();
	}
	
	public void initSystem(World world) {
		Objects.requireNonNull(world, "World must not be null.");
		
		this.world = world;
		
		if (world.entityManager.hasEntities()) {
			for (Entity entity : world.entityManager.getEntities()) {
				register(entity);
			}
		}
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
		if (collector.register(entity)) {
			if (runOnce && !processedEntityIds.contains(entity.getId())) {
				if (this instanceof InitSystem) {
					init(world.application.getWindow().id);
				} else if (this instanceof StartSystem) {
					start();
				}
			}
		}
	}
	
	/**
	 * Iterates over each component group in this system's collection.
	 */
	protected void forEach(Consumer<ComponentGroup> consumer) {
		for (ComponentGroup componentGroup : collector.componentGroups) {
			int entityId = componentGroup.getEntityId();
			boolean allowProcessing = componentGroup.isEnabled();
			
			if (runOnce && processedEntityIds.contains(entityId)) {
				allowProcessing = false;
			}
			
			if (allowProcessing) {
				try {
					consumer.accept(componentGroup);
				} catch (Exception e) {
					System.err.println("Failed to run system " + getClass().getName());
					e.printStackTrace();
				} finally {
					if (runOnce) {
						processedEntityIds.add(componentGroup.getEntityId());
					}
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
	
	public void print() {
		String systemName = getClass().getSimpleName();
		int groupCount = collector.componentGroups.size();
		
		System.out.printf("%s: (%s)%n", systemName, groupCount);
	}
}
