package dev.prozilla.pine.core.state;

import dev.prozilla.pine.common.lifecycle.Initializable;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Manages time tracking, including FPS (frames per second) and UPS (updates per second) calculations.
 */
public class ApplicationTimer implements Initializable {
	
	/** Timestamp of the previous loop, relative to the timestamp of the GLFW initialization. */
	private double previousLoopTime;
	private float currentLoopTime;
	
	/** Total time elapsed since GLFW was initialized in seconds, affected by <code>timeScale</code>.  */
	private float scaledElapsedTime;
	/** The scale at which time passes. */
	public float timeScale;
	
	/** Frames per second. */
	private int fps;
	/** Counter for the FPS calculation. */
	private int fpsCount;
	/** Updates per second. */
	private int ups;
	/** Counter for the UPS calculation. */
	private int upsCount;
	
	/** Time elapsed since last loop in seconds. */
	private float deltaTime;
	/** Time elapsed since last loop in seconds, multiplied with <code>timeScale</code>. */
	private float scaledDeltaTime;
	
	@Override
	public void init() {
		previousLoopTime = getCurrentTime();
		currentLoopTime = 0f;
		scaledElapsedTime = 0f;
		timeScale = 1f;
		
		fps = 0;
		fpsCount = 0;
		ups = 0;
		upsCount = 0;
	}
	
	/**
	 * Updates the timer's delta values and elapsed time values.
	 */
	public void update() {
		double time = getCurrentTime();
		deltaTime = (float)(time - previousLoopTime);
		
		previousLoopTime = time;
		currentLoopTime += deltaTime;
		
		scaledDeltaTime = deltaTime * timeScale;
		scaledElapsedTime += scaledDeltaTime;
	}
	
	public void incrementFPS() {
		fpsCount++;
	}
	
	public void incrementUPS() {
		upsCount++;
	}
	
	/**
	 * Updates FPS and UPS if a whole second has passed.
	 */
	public void checkCounters() {
		if (currentLoopTime > 1f) {
			fps = fpsCount;
			fpsCount = 0;
			
			ups = upsCount;
			upsCount = 0;
			
			currentLoopTime -= 1f;
		}
	}
	
	/**
	 * @return Frames per second
	 */
	public int getFPS() {
		return fps > 0 ? fps : fpsCount;
	}
	
	/**
	 * @return Updates per second
	 */
	public int getUPS() {
		return ups > 0 ? ups : upsCount;
	}
	
	/**
	 * @return Time elapsed since GLFW was initialized in seconds.
	 */
	public double getCurrentTime() {
		return glfwGetTime();
	}
	
	public float getScaledElapsedTime() {
		return scaledElapsedTime;
	}
	
	public float getDeltaTime() {
		return deltaTime;
	}
	
	public float getScaledDeltaTime() {
		return scaledDeltaTime;
	}
}