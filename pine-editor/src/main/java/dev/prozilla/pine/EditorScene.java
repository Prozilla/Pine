package dev.prozilla.pine;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.entity.canvas.EditorCanvasPrefab;
import dev.prozilla.pine.system.canvas.PreviewFrameUpdater;

public class EditorScene extends Scene {
	
	private final Application preview;
	
	public EditorScene(Application preview) {
		super("Editor");
		this.preview = preview;
	}
	
	@Override
	protected void load() {
		super.load();
		
		world.addSystem(new PreviewFrameUpdater());
		
		world.addEntity(new EditorCanvasPrefab(preview));
	}
}
