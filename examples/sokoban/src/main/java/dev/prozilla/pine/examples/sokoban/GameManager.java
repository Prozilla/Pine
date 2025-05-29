package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;
import dev.prozilla.pine.core.state.input.Key;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public Font font;
	
	public int completedCrates;
	public int totalCrates;
	
	private static final boolean ENABLE_MUSIC = false;
	
	public GameManager(Application application) {
		super(application);
		
		completedCrates = 0;
		totalCrates = 0;
	}
	
	@Override
	public void onInit(long window) {
		font = AssetPools.fonts.load("fonts/poppins/Poppins-Regular.ttf");
		
		// Play background music
		if (ENABLE_MUSIC || !Application.isDevMode()) {
			AudioSource music = AssetPools.audioSources.load("audio/pixel-playground.ogg");
			music.init();
			music.setVolume(0.21f);
			music.setLoop(true);
			music.play();
		}
	}
	
	@Override
	public void onInput(float deltaTime) {
		if (getInput().getKeyDown(Key.R)) {
			application.reloadScene();
		}
	}
}
