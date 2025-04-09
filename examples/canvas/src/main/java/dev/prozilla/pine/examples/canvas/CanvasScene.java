package dev.prozilla.pine.examples.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.CanvasPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.ContainerPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;

public class CanvasScene extends Scene {
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
		CanvasPrefab canvasPrefab = new CanvasPrefab();
		
		ContainerPrefab menuPrefab = new ContainerPrefab();
		menuPrefab.setGap(16);
		menuPrefab.setAnchor(GridAlignment.CENTER);
		menuPrefab.setAlignment(EdgeAlignment.CENTER);
		menuPrefab.setDirection(Direction.DOWN);
		menuPrefab.setBackgroundColor(Color.white().setAlpha(0.65f));
		menuPrefab.setPadding(new DualDimension(16));
		
		TextPrefab titleTextPrefab = new TextPrefab(application.getConfig().window.title.get());
		
		TextButtonPrefab textButtonPrefab = new TextButtonPrefab("This is a button");
		textButtonPrefab.setPadding(new DualDimension(16, 8));
		textButtonPrefab.setClickCallback((entity) -> {
			logger.log("Button clicked");
		});
		
		TextPrefab textPrefab = new TextPrefab("This is a text element");
		
		// Instantiate prefabs
		Entity canvas = canvasPrefab.instantiate(world);
		Entity menu = canvas.addChild(menuPrefab.instantiate(world));
		menu.addChild(titleTextPrefab.instantiate(world));
		menu.addChild(textPrefab.instantiate(world));
		menu.addChild(textButtonPrefab.instantiate(world));
		
		world.addEntity(canvas);
	}
}
