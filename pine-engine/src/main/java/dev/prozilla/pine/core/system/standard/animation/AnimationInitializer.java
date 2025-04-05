package dev.prozilla.pine.core.system.standard.animation;

import dev.prozilla.pine.core.component.AnimationData;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class AnimationInitializer extends InitSystem {
	
	public AnimationInitializer() {
		super(AnimationData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		AnimationData animationData = chunk.getComponent(AnimationData.class);
		animationData.restartAnimation();
	}
}
