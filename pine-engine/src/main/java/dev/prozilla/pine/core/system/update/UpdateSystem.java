package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;

/**
 * System for updating entity data.
 */
public abstract class UpdateSystem extends UpdateSystemBase {
	
	protected float timeScale;
	protected boolean applyTimeScale;
	
	@SafeVarargs
	public UpdateSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
		
		applyTimeScale = false;
	}
	
	public void setApplyTimeScale(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
		setRunWhenPaused(applyTimeScale);
	}
	
	@Override
	public final void update(float deltaTime) {
		timeScale = application.getTimer().timeScale;
		
		if (applyTimeScale) {
			deltaTime *= timeScale;
		}
		
		float finalDeltaTime = deltaTime;
		forEach(chunk -> process(chunk, finalDeltaTime));
	}
	
	/**
	 * Updates a single entity's data, each frame.
	 * @param chunk Entity that matches this system's query
	 * @param deltaTime Delta time in seconds
	 */
	protected abstract void process(EntityChunk chunk, float deltaTime);
}
