package dev.prozilla.pine.core.state;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.lifecycle.Updatable;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Manages time tracking, including FPS (frames per second) and UPS (updates per second) calculations.
 */
public class Timer implements Initializable {
	
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
	
	private final List<TimedAction> timedActions;
	
	public Timer() {
		timedActions = new ArrayList<>();
	}
	
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
	public void nextFrame() {
		double time = getCurrentTime();
		deltaTime = (float)(time - previousLoopTime);
		
		previousLoopTime = time;
		currentLoopTime += deltaTime;
		
		scaledDeltaTime = deltaTime * timeScale;
		scaledElapsedTime += scaledDeltaTime;
	}
	
	public void updateTimedActions() {
		for (TimedAction timedAction : timedActions) {
			timedAction.update(deltaTime);
		}
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
	
	public Interval startInterval(Callback callback, float delay) {
		return startInterval(callback, delay, false);
	}
	
	public Interval startInterval(Callback callback, float delay, boolean applyTimeScale) {
		Interval interval = new Interval(this, callback, delay, applyTimeScale);
		timedActions.add(interval);
		return interval;
	}
	
	public Timeout startTimeout(Callback callback, float delay) {
		return startTimeout(callback, delay, false);
	}
	
	public Timeout startTimeout(Callback callback, float delay, boolean applyTimeScale) {
		Timeout timeout = new Timeout(this, callback, delay, applyTimeScale);
		timedActions.add(timeout);
		return timeout;
	}
	
	public RandomInterval startRandomInterval(Callback callback, float minDelay, float maxDelay) {
		return startRandomInterval(callback, minDelay, maxDelay, false);
	}
	
	public RandomInterval startRandomInterval(Callback callback, float minDelay, float maxDelay, boolean applyTimeScale) {
		RandomInterval randomInterval = new RandomInterval(this, callback, minDelay, maxDelay, applyTimeScale);
		timedActions.add(randomInterval);
		return randomInterval;
	}
	
	public void stopInterval(Interval interval) {
		stopTimedAction(interval);
	}
	
	public void stopTimeout(Timeout timeout) {
		stopTimedAction(timeout);
	}
	
	private void stopTimedAction(TimedAction timedAction) {
		timedActions.remove(timedAction);
	}
	
	protected abstract static class TimedAction implements Updatable, Destructible {
		
		protected final Timer timer;
		private final Callback callback;
		protected float delay;
		protected float elapsed_time;
		private boolean applyTimeScale;
		
		public TimedAction(Timer timer, Callback callback, float delay, boolean applyTimeScale) {
			this.timer = timer;
			this.callback = callback;
			this.delay = delay;
			this.applyTimeScale = applyTimeScale;
			elapsed_time = 0;
		}
		
		@Override
		public void update(float deltaTime) {
			if (applyTimeScale) {
				deltaTime *= timer.timeScale;
			}
			elapsed_time += deltaTime;
		}
		
		public void setApplyTimeScale(boolean applyTimeScale) {
			this.applyTimeScale = applyTimeScale;
		}
		
		protected void execute() {
			if (callback != null) {
				callback.run();
			}
		}
		
	}
	
	public static class Timeout extends TimedAction {
		
		protected Timeout(Timer timer, Callback callback, float delay, boolean applyTimeScale) {
			super(timer, callback, delay, applyTimeScale);
		}
		
		@Override
		public void update(float deltaTime) {
			super.update(deltaTime);
			
			if (elapsed_time >= delay) {
				execute();
				destroy();
			}
		}
		
		@Override
		public void destroy() {
			timer.stopTimeout(this);
		}
		
	}
	
	public static class Interval extends TimedAction {
		
		protected Interval(Timer timer, Callback callback, float delay, boolean applyTimeScale) {
			super(timer, callback, delay, applyTimeScale);
		}
		
		@Override
		public void update(float deltaTime) {
			super.update(deltaTime);
			
			// If more time has passed than the delay of this interval, the action needs to be executed multiple times
			while (elapsed_time >= delay) {
				execute();
			}
		}
		
		@Override
		protected void execute() {
			super.execute();
			elapsed_time -= delay;
		}
		
		@Override
		public void destroy() {
			timer.stopInterval(this);
		}
		
	}
	
	public static class RandomInterval extends Interval {
		
		private final RandomFloatProperty delayProperty;
		
		protected RandomInterval(Timer timer, Callback callback, float minDelay, float maxDelay, boolean applyTimeScale) {
			this(timer, callback, new RandomFloatProperty(minDelay, maxDelay), applyTimeScale);
		}
		
		protected RandomInterval(Timer timer, Callback callback, RandomFloatProperty delayProperty, boolean applyTimeScale) {
			super(timer, callback, delayProperty.getValue(), applyTimeScale);
			this.delayProperty = delayProperty;
		}
		
		@Override
		protected void execute() {
			super.execute();
			delay = delayProperty.getValue();
		}
		
	}
	
}