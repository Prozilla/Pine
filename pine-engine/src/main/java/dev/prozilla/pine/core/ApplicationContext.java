package dev.prozilla.pine.core;

import dev.prozilla.pine.common.ContextOf;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.audio.AudioDevice;
import dev.prozilla.pine.core.mod.ModManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.ApplicationTimer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.input.Input;

@ContextOf(Application.class)
public interface ApplicationContext {
	
	Input getInput();
	
	Window getWindow();
	
	Renderer getRenderer();
	
	ApplicationTimer getTimer();
	
	Tracker getTracker();
	
	Config getConfig();
	
	ModManager getModManager();
	
	Logger getLogger();
	
	AudioDevice getAudioDevice();
	
}
