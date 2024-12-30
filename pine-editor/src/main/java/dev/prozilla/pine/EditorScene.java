package dev.prozilla.pine;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.Scene;
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
		
		cameraData.setBackgroundColor(Color.BLUE);
		
		world.addSystem(new PreviewFrameUpdater());
		
		world.addEntity(new EditorCanvasPrefab(preview));
	}
}
