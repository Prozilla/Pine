package dev.prozilla.pine.examples.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.scene.Scene;

public class MainScene extends Scene {
	
	@Override
	protected void load() {
		super.load();
		
		// Create prefabs
		NodeRootPrefab nodeRootPrefab = new NodeRootPrefab();
		
		LayoutPrefab menuPrefab = new LayoutPrefab();
		menuPrefab.setGap(new Dimension(16));
		menuPrefab.setAnchor(GridAlignment.CENTER);
		menuPrefab.setAlignment(EdgeAlignment.CENTER);
		menuPrefab.setDirection(Direction.DOWN);
		menuPrefab.setBackgroundColor(Color.white().setAlpha(0.65f));
		menuPrefab.setPadding(new DualDimension(16));
		
		TextPrefab titleTextPrefab = new TextPrefab(application.getConfig().window.title.get());
		titleTextPrefab.setColor(Color.black());
		
		TextButtonPrefab textButton1Prefab = new TextButtonPrefab("This is a button");
		textButton1Prefab.setPadding(new DualDimension(16, 8));
		textButton1Prefab.setClickCallback((entity) -> logger.log("Button 1 clicked"));
		
		TextButtonPrefab textButton2Prefab = new TextButtonPrefab("This is also a button");
		textButton2Prefab.setPadding(new DualDimension(16, 8));
		textButton2Prefab.setClickCallback((entity) -> logger.log("Button 2 clicked"));
		
		TextButtonPrefab textButton3Prefab = new TextButtonPrefab("This is another a button");
		textButton3Prefab.setPadding(new DualDimension(16, 8));
		textButton3Prefab.setClickCallback((entity) -> logger.log("Button 3 clicked"));
		
		
		TextPrefab textPrefab = new TextPrefab("This is a text element");
		textPrefab.setColor(Color.black());
		
		// Instantiate prefabs
		Entity nodeRoot = nodeRootPrefab.instantiate(world);
		Entity menu = nodeRoot.addChild(menuPrefab);
		menu.addChild(titleTextPrefab);
		menu.addChild(textPrefab);
		menu.addChild(textButton1Prefab);
		menu.addChild(textButton2Prefab);
		menu.addChild(textButton3Prefab);
		
		world.addEntity(nodeRoot);
	}
}
