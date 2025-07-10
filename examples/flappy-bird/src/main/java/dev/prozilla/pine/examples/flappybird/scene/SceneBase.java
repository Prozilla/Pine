package dev.prozilla.pine.examples.flappybird.scene;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;
import dev.prozilla.pine.examples.flappybird.entity.BackgroundPrefab;
import dev.prozilla.pine.examples.flappybird.system.background.BackgroundInitializer;

public abstract class SceneBase extends Scene {
	
	// Common resources
	public Font font;
	
	@Override
	protected void load() {
		super.load();
		
		font = AssetPools.fonts.load("flappybird/flappy-bird.ttf", 32);
		
		// Create prefabs
		BackgroundPrefab backgroundPrefab = new BackgroundPrefab();
		
		// Add systems
		world.addSystem(new BackgroundInitializer());
		
		// Fill screen with background sprites
		int backgroundCount = Math.round((float)FlappyBird.WIDTH / BackgroundData.WIDTH + 0.5f) + 1;
		for (int i = 0; i < backgroundCount; i++) {
			world.addEntity(backgroundPrefab.instantiate(world, i));
		}
	}
	
}
