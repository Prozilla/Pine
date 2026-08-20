package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;
import dev.prozilla.pine.examples.sokoban.scene.GameScene;
import dev.prozilla.pine.examples.sokoban.scene.MenuScene;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public Font font;
	
	public int completedCrates;
	public int totalCrates;
	
	private SessionConfig sessionConfig;
	
	public static final boolean ENABLE_MUSIC = false;
	public static final int TILE_SIZE = 64;
	public static final Color BACKGROUND_COLOR = Color.hex("#596A6C");
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public GameManager(Application application) {
		super(application);
		
		completedCrates = 0;
		totalCrates = 0;
		
		instance = this;
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
	
	public void hostGame(int port) {
		sessionConfig = new SessionConfig(true, null, port);
		application.loadScene(new GameScene());
	}
	
	public void joinGame(String address, int port) {
		sessionConfig = new SessionConfig(false, address, port);
		application.loadScene(new GameScene());
	}
	
	public SessionConfig getSessionConfig() {
		return sessionConfig;
	}
	
	public boolean isMultiplayer() {
		return sessionConfig != null;
	}
	
	public void leaveSession() {
		sessionConfig = null;
		application.loadScene(new MenuScene());
	}
	
	public record SessionConfig(boolean hosting, String address, int port) {}
	
}
