package dev.prozilla.pine.core;

import dev.prozilla.pine.common.ContextOf;
import dev.prozilla.pine.common.logging.AppLogger;
import dev.prozilla.pine.core.audio.AudioDevice;
import dev.prozilla.pine.core.mod.ModManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.storage.LocalStorage;

@ContextOf(Application.class)
public interface ApplicationContext {
	
	Input getInput();
	
	Window getWindow();
	
	Renderer getRenderer();
	
	Timer getTimer();
	
	Tracker getTracker();
	
	Config getConfig();
	
	ModManager getModManager();
	
	AppLogger getLogger();
	
	AudioDevice getAudioDevice();
	
	LocalStorage getLocalStorage();
	
}
