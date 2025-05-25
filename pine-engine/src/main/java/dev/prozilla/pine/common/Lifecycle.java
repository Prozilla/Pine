package dev.prozilla.pine.common;

import dev.prozilla.pine.core.rendering.Renderer;

/**
 * Defines the lifecycle methods for various objects.
 * Methods of the lifecycle are called in the following order:
 * <ul>
 *     <li>
 *         Before lifetime:
 *         <ol>
 *             <li><code>init()</code></li>
 *         </ol>
 *     </li>
 *     <li>
 *         During lifetime, once per frame:
 *         <ol>
 *             <li><code>input()</code></li>
 *             <li><code>update()</code></li>
 *             <li><code>render()</code></li>
 *         </ol>
 *     </li>
 *     <li>
 *         After lifetime:
 *         <ol>
 *             <li><code>destroy()</code></li>
 *         </ol>
 *     </li>
 * </ul>
 */
public interface Lifecycle {
	
	/**
	 * Initializes the object before the game loop starts.
	 */
	default void init() {
		throw new UnsupportedOperationException("Init method without parameters not supported");
	}
	
	/**
	 * Initializes the object before the game loop starts.
	 * @param window Window
	 */
	default void init(long window) {
		throw new UnsupportedOperationException("Init method with parameters not supported");
	}
	
	/**
	 * Handles input every frame, before update.
	 */
	default void input() {
		throw new UnsupportedOperationException("Input method without parameters not supported");
	}
	
	/**
	 * Handles input every frame, before update.
	 * @param deltaTime Delta time in seconds
	 */
	default void input(float deltaTime) {
		throw new UnsupportedOperationException("Init method with parameters not supported");
	}
	
	/**
	 * Updates the object every frame.
	 */
	default void update() {
		throw new UnsupportedOperationException("Update method without parameters not supported");
	}
	
	/**
	 * Updates the object every frame.
	 * @param deltaTime Delta time in seconds
	 */
	default void update(float deltaTime) {
		throw new UnsupportedOperationException("Update method with parameters not supported");
	}
	
	/**
	 * Renders the object every frame.
	 */
	default void render() {
		throw new UnsupportedOperationException("Render method without parameters not supported");
	}
	
	/**
	 * Renders the object every frame.
	 * @param renderer Reference to the renderer
	 */
	default void render(Renderer renderer) {
		throw new UnsupportedOperationException("Render method with parameters not supported");
	}
	
	/**
	 * Destroys the object at the end of the game loop.
	 */
	default void destroy() {
		throw new UnsupportedOperationException("Destroy method not supported");
	}
	
}
