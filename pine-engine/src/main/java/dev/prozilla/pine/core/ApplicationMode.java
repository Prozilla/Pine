package dev.prozilla.pine.core;

import dev.prozilla.pine.core.audio.AudioDevice;
import dev.prozilla.pine.core.audio.HeadlessAudioDevice;
import dev.prozilla.pine.core.rendering.HeadlessRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.HeadlessTimer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.input.HeadlessInput;
import dev.prozilla.pine.core.state.input.Input;

public enum ApplicationMode {
	/** A standalone application renders inside its own window. */
	STANDALONE(true, true),
	/** An embedded application is rendered inside a frame buffer in another application. */
	EMBEDDED(false, true),
	/** A headless application does not render anywhere. */
	HEADLESS(false, false) {
		@Override
		public Window createWindow(Application application) {
			return new HeadlessWindow(application);
		}
		
		@Override
		public Timer createTimer() {
			return new HeadlessTimer();
		}
		
		@Override
		public Renderer createRenderer(Application application) {
			return new HeadlessRenderer(application);
		}
		
		@Override
		public AudioDevice createAudioDevice(Application application) {
			return new HeadlessAudioDevice(application);
		}
		
		@Override
		public Input createInput(Application application) {
			return new HeadlessInput(application);
		}
	};
	
	public final boolean usesOpenGL;
	public final boolean renders;
	
	ApplicationMode(boolean usesOpenGL, boolean renders) {
		this.usesOpenGL = usesOpenGL;
		this.renders = renders;
	}
	
	public Window createWindow(Application application) {
		return new Window(application);
	}
	
	public Timer createTimer() {
		return new Timer();
	}
	
	public Renderer createRenderer(Application application) {
		return new Renderer(application);
	}
	
	public AudioDevice createAudioDevice(Application application) {
		return new AudioDevice(application);
	}
	
	public Input createInput(Application application) {
		return new Input(application);
	}
	
}
