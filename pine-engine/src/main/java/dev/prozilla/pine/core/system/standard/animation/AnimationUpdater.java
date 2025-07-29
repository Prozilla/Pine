package dev.prozilla.pine.core.system.standard.animation;

import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public final class AnimationUpdater extends UpdateSystem {
	
	public AnimationUpdater() {
		super(AnimationData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		AnimationData animationData = chunk.getComponent(AnimationData.class);

		if (animationData.applyTimeScale) {
			animationData.updateAnimation(deltaTime * timeScale);
		} else {
			animationData.updateAnimation(deltaTime);
		}
	}
	
}
