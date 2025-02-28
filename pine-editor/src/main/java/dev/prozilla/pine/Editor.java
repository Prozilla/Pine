package dev.prozilla.pine;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.config.WindowConfig;

public class Editor extends Application {
	
	private final Application preview;
	
	public Editor(Application application) {
		super("Pine Editor - " + application.getConfig().window.title.get(), 1200, 900, new EditorScene(application), 120);
		this.preview = application;
		
		// Make sure preview is synced to editor
		this.preview.getConfig().copyFrom(this.getConfig(), Config.FPS);
	}
	
	@Override
	public void run() {
		System.out.println("Running editor");
		super.run();
	}
	
	@Override
	public void init() {
		super.init();
		WindowConfig config = preview.getConfig().window;
		preview.initPreview(input, config.width.get(), config.height.get());
	}
	
	@Override
	public void update(float deltaTime) {
		try {
			preview.updatePreview(0);
		} catch (Exception e) {
			System.err.println("Failed to update preview");
			e.printStackTrace();
		} finally {
			super.update(deltaTime);
		}
	}
	
	@Override
	public void render(Renderer renderer) {
		try {
			preview.renderPreview();
		} catch (Exception e) {
			System.err.println("Failed to render preview");
			e.printStackTrace();
		} finally {
			
			super.render(renderer);
		}
	}
	
	@Override
	public void destroy() {
		try {
			preview.destroy();
		} catch (Exception e) {
			System.err.println("Failed to destroy preview");
			e.printStackTrace();
		} finally {
			super.destroy();
		}
	}
}
