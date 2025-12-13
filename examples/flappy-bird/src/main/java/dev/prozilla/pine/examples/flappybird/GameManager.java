package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public int seed;
	
	public GameManager(Application application) {
		super(application);
		
		// Load seed
		seed = application.getLocalStorage().getInt("seed", 1);
		
		instance = this;
	}
	
	@Override
	public void onInit(long window) {
		super.onInit(window);
		
		AssetPools.textures.createTextureArray(96, 96);
	}
	
}
