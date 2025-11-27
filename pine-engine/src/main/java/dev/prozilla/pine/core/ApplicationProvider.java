package dev.prozilla.pine.core;

import dev.prozilla.pine.common.ProviderOf;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.audio.AudioDevice;
import dev.prozilla.pine.core.mod.ModManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.storage.LocalStorage;

/**
 * Provides access to the application and all its helper classes.
 */
@ProviderOf(Application.class)
@FunctionalInterface
public interface ApplicationProvider extends ApplicationContext {
	
	Application getApplication();
	
	@Override
	default Input getInput() {
		return getApplication().getInput();
	}
	
	@Override
	default Window getWindow() {
		return getApplication().getWindow();
	}
	
	@Override
	default Renderer getRenderer() {
		return getApplication().getRenderer();
	}
	
	@Override
	default Timer getTimer() {
		return getApplication().getTimer();
	}
	
	@Override
	default Tracker getTracker() {
		return getApplication().getTracker();
	}
	
	@Override
	default Config getConfig() {
		return getApplication().getConfig();
	}
	
	@Override
	default ModManager getModManager() {
		return getApplication().getModManager();
	}
	
	@Override
	default Logger getLogger() {
		return getApplication().getLogger();
	}
	
	@Override
	default AudioDevice getAudioDevice() {
		return getApplication().getAudioDevice();
	}
	
	@Override
	default LocalStorage getLocalStorage() {
		return  getApplication().getLocalStorage();
	}
	
	static void stopApplication(ApplicationProvider applicationProvider) {
		applicationProvider.getApplication().stop();
	}
	
	static void reloadScene(ApplicationProvider applicationProvider) {
		applicationProvider.getApplication().reloadScene();
	}
	
	static void nextScene(ApplicationProvider applicationProvider) {
		applicationProvider.getApplication().nextScene();
	}
	
}
