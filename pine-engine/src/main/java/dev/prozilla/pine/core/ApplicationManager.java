package dev.prozilla.pine.core;

import dev.prozilla.pine.core.rendering.Renderer;

public abstract class ApplicationManager implements ApplicationProvider {
	
	protected final Application application;
	
	public ApplicationManager(Application application) {
		this.application = application;
	}
	
	public void onInit(long window) {
	
	}
	
	public void onStart() {
	
	}
	
	public void onInput(float deltaTime) {
	
	}
	
	public void onUpdate(float deltaTime) {
	
	}
	
	public void onRender(Renderer renderer) {
	
	}
	
	public void onDestroy() {
	
	}
	
	public void onPause() {
	
	}
	
	public void onResume() {
	
	}
	
	@Override
	public Application getApplication() {
		return application;
	}
}
