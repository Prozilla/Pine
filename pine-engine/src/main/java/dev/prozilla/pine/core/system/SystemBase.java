package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.state.Scene;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

// TO DO: ignore de-activated components

/**
 * System responsible for handling logic and behaviour for entities that match a query based on their components.
 */
public abstract class SystemBase implements Lifecycle {
	
	private final Class<? extends Component>[] componentTypes;
	/** Query that entities must match in order to be processed by this system. */
	private EntityQuery query;
	/** If true, this system will only process each entity once. */
	private final boolean runOnce;
	
	/**
	 * Keeps track of entities that have already been processed,
	 * if <code>runOnce</code> is set to <code>true</code>.
	 */
	private final ArrayList<Integer> processedEntityIds;
	
	protected World world;
	protected Application application;
	protected Scene scene;
	
	public SystemBase(Class<? extends Component>... componentTypes) {
		this(componentTypes, false);
	}
	
	public SystemBase(Class<? extends Component>[] componentTypes, boolean runOnce) {
		Objects.requireNonNull(componentTypes, "Array of componentTypes must not be null.");
		
		this.componentTypes = componentTypes;
		this.runOnce = runOnce;
		
		processedEntityIds = new ArrayList<>();
	}
	
	public void initSystem(World world) {
		Objects.requireNonNull(world, "World must not be null.");
		
		this.world = world;
		application = world.application;
		scene = world.scene;
		
		// Create entity query
		query = world.queryPool.getQuery(componentTypes, runOnce);
		
		// Process existing entities
		if (world.entityManager.hasEntities()) {
			for (Entity entity : world.entityManager.getEntities()) {
				register(entity);
			}
		}
	}
	
	/**
	 * Registers an entity in this system's query.
	 * @see EntityQuery
	 */
	public void register(Entity entity) {
		if (query.register(entity)) {
			if (runOnce && !processedEntityIds.contains(entity.getId())) {
				if (this instanceof InitSystem) {
					init(application.getWindow().id);
				}
			}
		}
	}
	
	/**
	 * Iterates over each entity that matches the query of this system.
	 */
	protected void forEach(Consumer<EntityMatch> consumer) {
		for (EntityMatch entityMatch : query.entityMatches) {
			int entityId = entityMatch.getEntityId();
			boolean allowProcessing = entityMatch.isActive();
			
			if (runOnce && processedEntityIds.contains(entityId)) {
				allowProcessing = false;
			}
			
			if (allowProcessing) {
				try {
					consumer.accept(entityMatch);
				} catch (Exception e) {
					System.err.println("Failed to run system " + getClass().getName());
					e.printStackTrace();
				} finally {
					if (runOnce) {
						processedEntityIds.add(entityMatch.getEntityId());
					}
				}
			}
		}
	}
	
	/**
	 * Returns true if this system has collected any component groups.
	 * @see EntityQuery
	 */
	public boolean hasComponentGroups() {
		return !query.entityMatches.isEmpty();
	}
	
	public void print() {
		String systemName = getClass().getSimpleName();
		int groupCount = query.entityMatches.size();
		
		System.out.printf("%s: (%s)%n", systemName, groupCount);
	}
}
