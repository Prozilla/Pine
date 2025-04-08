package dev.prozilla.pine.common.math.easing;

/**
 * An easing function that jumps between steps with an equal length instead of transitioning smoothly.
 */
public class StepEasing implements EasingFunction {
	
	protected final int steps;
	protected final boolean jumpAtStart;
	
	public StepEasing(int steps) {
		this(steps, false);
	}
	
	public StepEasing(int steps, boolean jumpAtStart) {
		this.steps = steps;
		this.jumpAtStart = jumpAtStart;
	}
	
	@Override
	public float get(float t) {
		if (jumpAtStart) {
			return (float)Math.floor(t * steps) / steps;
		} else {
			return (float)Math.ceil(t * steps) / steps;
		}
	}
	
	@Override
	public String toString() {
		return String.format("steps(%s, %s)", steps, jumpAtStart ? "start" : "end");
	}
	
}
