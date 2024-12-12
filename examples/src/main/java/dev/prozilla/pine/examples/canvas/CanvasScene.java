package dev.prozilla.pine.examples.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.CanvasPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.ContainerPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;
import dev.prozilla.pine.core.state.Scene;

public class CanvasScene extends Scene {
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
		CanvasPrefab canvasPrefab = new CanvasPrefab();
		
		ContainerPrefab menuPrefab = new ContainerPrefab();
		menuPrefab.setGap(16);
		menuPrefab.setAnchor(RectTransform.Anchor.CENTER);
		menuPrefab.setAlignment(CanvasGroup.Alignment.CENTER);
		menuPrefab.setDirection(CanvasGroup.Direction.DOWN);
		menuPrefab.setBackgroundColor(Color.WHITE.clone().setAlpha(0.65f));
		menuPrefab.setPadding(16, 16);
		
		TextPrefab titleTextPrefab = new TextPrefab(application.title);
		
		TextButtonPrefab textButtonPrefab = new TextButtonPrefab("This is a button");
		textButtonPrefab.setPadding(16, 8);
		textButtonPrefab.setClickCallback(() -> {
			System.out.println("Button clicked");
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
