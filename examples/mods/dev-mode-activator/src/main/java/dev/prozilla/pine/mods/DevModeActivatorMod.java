package dev.prozilla.pine.mods;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.mod.Mod;

public class DevModeActivatorMod implements Mod {
	
	@Override
	public void init(Application application) {
		Application.setDevMode(true);
	}
	
}