package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.system.init.InitSystemBase;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Base class for system responsible for handling logic and behaviour for entities that match a query based on their components.
 */
public abstract class SystemBase {
	
	private final Class<? extends Component>[] includedComponentTypes;
	private Class<? extends Component>[] excludedComponentTypes;
	/** Query that entities must match in order to be processed by this system. */
	private EntityQuery query;
	/** When set to <code>true</code>, this system will only process each entity once. */
	private final boolean runOnce;
	/** When set to <code>false</code>, this system will not process entities when the application is paused. */
	private boolean runWhenPaused;
	private String entityTag;
	
	/**
	 * Keeps track of entities that have already been processed,
	 * if <code>runOnce</code> is set to <code>true</code>.
	 */
	private final Set<Integer> processedEntityIds;
	
	protected World world;
	protected Application application;
	protected Scene scene;
	protected Logger logger;
	
	public SystemBase(Class<? extends Component>[] componentTypes) {
		this(componentTypes, false);
	}
	
	public SystemBase(Class<? extends Component>[] componentTypes, boolean runOnce) {
		Objects.requireNonNull(componentTypes, "Array of componentTypes must not be null.");
		
		includedComponentTypes = componentTypes;
		this.runOnce = runOnce;
		runWhenPaused = true;
		
		processedEntityIds = new HashSet<>();
	}
	
	/**
	 * Restricts this system's query to entities with a given tag.
	 * If multiple entities have overlapping component types and you only want this system to process some of them,
	 * you can use tags to select the right entities.
	 * @throws IllegalStateException If the query has already been created.
	 * @see EntityQuery
	 */
	protected void setRequiredTag(String tag) throws IllegalStateException {
		if (query != null) {
			throw new IllegalStateException("Required tag must be set before the query creation.");
		}
		
		this.entityTag = tag;
	}
	
	@SafeVarargs
	protected final void setExcludedComponentTypes(Class<? extends Component>... componentTypes) throws IllegalArgumentException {
		if (query != null) {
			throw new IllegalStateException("Excluded component types must be set before the query creation.");
		}
		
		if (excludedComponentTypes != null && includedComponentTypes != null) {
			Checks.areDisjunct(excludedComponentTypes, includedComponentTypes, "excludedComponentTypes and includedComponentTypes must be disjunct");
		}
		
		excludedComponentTypes = componentTypes;
	}
	
	protected void setRunWhenPaused(boolean runWhenPaused) {
		this.runWhenPaused = runWhenPaused;
	}
	
	/**
	 * Initializes this system and creates the query.
	 * If there are already entities in the world, this will register each entity in this system.
	 */
	public void initSystem(World world) {
		Objects.requireNonNull(world, "world must not be null");
		
		this.world = world;
		application = world.application;
		scene = world.scene;
		logger = application.getLogger();
		
		// Create entity query
		query = world.queryPool.getQuery(includedComponentTypes, excludedComponentTypes, runOnce, entityTag);
		
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
			if (runOnce && !processedEntityIds.contains(entity.id)) {
				if (this instanceof InitSystemBase initSystemBase && world.initialized) {
					initSystemBase.init();
				}
			}
		}
	}
	
	/**
	 * Unregisters an entity from this system's query.
	 * @see EntityQuery
	 */
	public void unregister(Entity entity) {
		if (runOnce) {
			processedEntityIds.remove(entity.id);
		}
		
		query.unregister(entity);
	}
	
	/**
	 * Iterates over each entity that matches the query of this system.
	 * @param action Action to perform on each entity
	 */
	protected void forEach(Consumer<EntityChunk> action) {
		try {
			query.startIteration();
			int count = query.entityChunks.size();
			for (int i = 0; i < count; i++) {
				if (!scene.isActive()) {
					// Abort if the scene was unloaded during the iteration
					break;
				}
				
				EntityChunk entityChunk = query.entityChunks.get(i);
				if (entityChunk.isActive()) {
					accept(entityChunk, action);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to iterate over entities in system: " + getClass().getSimpleName(), e);
		} finally {
			if (scene.isActive()) {
				query.endIteration();
			}
		}
	}
	
	/**
	 * Iterates over each entity that matches the query of this system in reverse.
	 * @param action Action to perform on each entity
	 */
	protected void forEachReverse(Consumer<EntityChunk> action) {
		try {
			query.startIteration();
			int count = query.entityChunks.size();
			for (int i = count - 1; i >= 0; i--) {
				if (!scene.isActive()) {
					// Abort if the scene was unloaded during the iteration
					break;
				}
				
				EntityChunk entityChunk = query.entityChunks.get(i);
				if (entityChunk.isActive()) {
					accept(entityChunk, action);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to iterate over entities in system: " + getClass().getSimpleName(), e);
		} finally {
			if (scene.isActive()) {
				query.endIteration();
			}
		}
	}
	
	/**
	 * Performs an action on a single entity chunk in this system.
	 * @param entityChunk Entity chunk to perform action on
	 * @param action Action to perform on entity chunk
	 */
	private void accept(EntityChunk entityChunk, Consumer<EntityChunk> action) throws NullPointerException {
		Objects.requireNonNull(entityChunk, "Entity chunk must not be null.");
		Objects.requireNonNull(action, "Action must not be null.");
		
		if (!entityChunk.isActive() || (runOnce && processedEntityIds.contains(entityChunk.getEntity().id))) {
			return;
		}
		
		try {
			action.accept(entityChunk);
		} catch (Exception e) {
			logger.error("Failed to run action on entity in system: " + getClass().getSimpleName(), e);
		} finally {
			if (runOnce) {
				processedEntityIds.add(entityChunk.getEntity().id);
			}
		}
	}
	
	/**
	 * Sorts the entity chunks in this system based on a comparator.
	 */
	protected void sort(Comparator<EntityChunk> comparator) {
		query.entityChunks.sort(comparator);
	}
	
	/**
	 * Returns true if this system has any entity chunks.
	 * @see EntityQuery
	 */
	public boolean hasEntityChunks() {
		return query.hasEntityChunks();
	}
	
	public boolean shouldRun() {
		return hasEntityChunks() && (runWhenPaused || !application.isPaused());
	}
	
	public void print() {
		String systemName = getClass().getSimpleName();
		int groupCount = query.entityChunks.size();
		
		logger.logf("%s: (%s)", systemName, groupCount);
	}
}
