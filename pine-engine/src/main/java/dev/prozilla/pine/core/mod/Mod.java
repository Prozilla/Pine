package dev.prozilla.pine.core.mod;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.Input;

/**
 * Interface for application modifications (mods).
 * A mod has the possibility to inject logic before, after or during a step in the lifecycle of the application.
 */
public interface Mod {
	
	/**
	 * Initializes this mod.
	 * @param application The application this mod is being loaded into
	 */
	default void init(Application application) {}
	
	/**
	 * Runs before the application receives input, every frame.
	 * @param input System that handles input
	 * @param deltaTime Time between this frame and the previous one, in seconds
	 */
	default void beforeInput(Input input, float deltaTime) {}
	
	/**
	 * Runs after the application has received input, every frame.
	 * @param input System that handles input
	 * @param deltaTime Time between this frame and the previous one, in seconds
	 */
	default void afterInput(Input input, float deltaTime) {}
	
	/**
	 * Runs before the application receives an update, every frame.
	 * @param deltaTime Time between this frame and the previous one, in seconds
	 */
	default void beforeUpdate(float deltaTime) {}
	
	/**
	 * Runs after the application has received an update, every frame.
	 * @param deltaTime Time between this frame and the previous one, in seconds
	 */
	default void afterUpdate(float deltaTime) {}
	
	/**
	 * Runs before the application is rendered, every frame.
	 * @param renderer System that handles rendering
	 */
	default void beforeRender(Renderer renderer) {}
	
	/**
	 * Runs after the application has been rendered, every frame.
	 * @param renderer System that handles rendering
	 */
	default void afterRender(Renderer renderer) {}
	
	/**
	 * Runs when the application is closed.
	 */
	default void destroy() {}

}
