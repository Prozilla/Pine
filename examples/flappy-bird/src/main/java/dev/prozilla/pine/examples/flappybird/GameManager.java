package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public int seed;
	public int playerVariant;
	public int pipeVariant;
	public int backgroundVariant;
	
	public GameManager(Application application) {
		super(application);
		
		// Load seed
		seed = application.getLocalStorage().getInt("seed", 1);
		playerVariant = 2;
		pipeVariant = 1;
		backgroundVariant = 0;
		
		instance = this;
	}
	
	@Override
	public void onInit(long window) {
		super.onInit(window);
		
		AssetPools.textures.createTextureArray(96, 96);
	}
	
}
