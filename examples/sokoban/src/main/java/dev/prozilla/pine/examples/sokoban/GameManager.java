package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;

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
	}
}
