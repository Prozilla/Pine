package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationManager;
import dev.prozilla.pine.core.storage.LocalStorage;

public class GameManager extends ApplicationManager {
	
	public static GameManager instance;
	
	public int seed;
	
	public GameManager(Application application) {
		super(application);
		
		// Load seed
		LocalStorage localStorage = application.getLocalStorage();
		if (localStorage.hasItem("seed")) {
			seed = application.getLocalStorage().getInt("seed");
		} else {
			seed = 1;
		}
		
		instance = this;
	}
	
}
