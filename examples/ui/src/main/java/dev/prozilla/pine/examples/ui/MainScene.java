package dev.prozilla.pine.examples.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

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
		
		TextButtonPrefab textButtonPrefab = new TextButtonPrefab("This is a button");
		textButtonPrefab.setPadding(new DualDimension(16, 8));
		textButtonPrefab.setClickCallback((entity) -> logger.log("Button clicked"));
		
		TextPrefab textPrefab = new TextPrefab("This is a text element");
		textPrefab.setColor(Color.black());
		
		// Instantiate prefabs
		Entity nodeRoot = nodeRootPrefab.instantiate(world);
		Entity menu = nodeRoot.addChild(menuPrefab.instantiate(world));
		menu.addChild(titleTextPrefab.instantiate(world));
		menu.addChild(textPrefab.instantiate(world));
		menu.addChild(textButtonPrefab.instantiate(world));
		
		world.addEntity(nodeRoot);
	}
}
