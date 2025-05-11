package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.audio.AudioSource;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;
import dev.prozilla.pine.core.state.input.Key;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public Font font;
	
	public int completedCrates;
	public int totalCrates;
	
	public GameManager(Application application) {
		super(application);
		
		completedCrates = 0;
		totalCrates = 0;
	}
	
	@Override
	public void onInit(long window) {
		font = ResourcePool.loadFont("fonts/poppins/Poppins-Regular.ttf");
		
		// Play background music
		AudioSource music = ResourcePool.loadAudioSource("audio/pixel-playground.ogg");
		music.init();
		music.setVolume(0.21f);
		music.setLoop(true);
		music.play();
	}
	
	@Override
	public void onInput(float deltaTime) {
		if (getInput().getKeyDown(Key.R)) {
			application.reloadScene();
		}
	}
}
