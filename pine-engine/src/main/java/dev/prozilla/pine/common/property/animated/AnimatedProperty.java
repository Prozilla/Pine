package dev.prozilla.pine.common.property.animated;

public abstract class AnimatedProperty<T> implements AnimatedPropertyBase<T> {
	
	protected final AnimationCurve curve;
	protected float time;
	
	protected AnimatedProperty(AnimationCurve curve) {
		this.curve = curve;
		restart();
	}
	
	@Override
	public void restart() {
		time = 0;
	}
	
	/**
	 * Progresses the animation.
	 * @param deltaTime How far to progress the animation, in seconds
	 */
	@Override
	public void update(float deltaTime) {
		time += deltaTime;
	}
	
	@Override
	public float getFactor() {
		return curve.evaluate(time);
	}
	
	@Override
	public boolean hasFinished() {
		return time >= curve.duration;
	}
	
	@Override
	public void setDuration(float duration) {
		curve.duration = duration;
	}
	
	@Override
	public AnimationCurve getCurve() {
		return curve;
	}
	
}
