package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Key;

public class MainScene extends Scene {
	
	private AudioSource source;
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.setBackgroundColor(Color.hsl(0f, 0f, 0.05f));
		
		source = AssetPools.audioSources.load("audio/KevinMacleod-PookatoriAndFriends.ogg");
		source.setCapture(true);
		source.init();
		source.setVolume(0.21f);
		source.setLoop(true);
		source.play();
		
		world.addSystem(new BarResizer(source));
		
		BarPrefab barPrefab = new BarPrefab();
		for (int i = 0; i < Main.BAR_COUNT; i++) {
			barPrefab.setIndex(i);
			world.addEntity(barPrefab);
		}
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		if (getInput().getKeyDown(Key.SPACE)) {
			source.togglePause();
		}
	}
	
}
