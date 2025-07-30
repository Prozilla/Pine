package dev.prozilla.pine.examples.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
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
		
		TextPrefab titleTextPrefab = new TextPrefab(application.getConfig().window.title.getValue());
		titleTextPrefab.setColor(Color.black());
		
		TextButtonPrefab textButton1Prefab = new TextButtonPrefab("This is a button");
		textButton1Prefab.setPadding(new DualDimension(16, 8));
		textButton1Prefab.setClickCallback((entity) -> logger.log("Button 1 clicked"));
		textButton1Prefab.setTooltipText("Button 1");
		
		TextButtonPrefab textButton2Prefab = new TextButtonPrefab("This is also a button");
		textButton2Prefab.setPadding(new DualDimension(16, 8));
		textButton2Prefab.setClickCallback((entity) -> logger.log("Button 2 clicked"));
		textButton2Prefab.setTooltipText("Button 2");
		
		TextButtonPrefab textButton3Prefab = new TextButtonPrefab("This is another a button");
		textButton3Prefab.setPadding(new DualDimension(16, 8));
		textButton3Prefab.setClickCallback((entity) -> logger.log("Button 3 clicked"));
		textButton3Prefab.setTooltipText("Button 3");
		
		TextPrefab textPrefab = new TextPrefab("This is a text element");
		textPrefab.setColor(Color.black());
		
		menuPrefab.addChildren(titleTextPrefab, textPrefab,
			textButton1Prefab, textButton2Prefab, textButton3Prefab);
		
		nodeRootPrefab.addChild(menuPrefab);
		
		// Instantiate prefabs
		world.addEntity(nodeRootPrefab);
	}
}
