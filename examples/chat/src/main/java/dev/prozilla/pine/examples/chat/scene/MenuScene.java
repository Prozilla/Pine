package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;

public class MenuScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setGap(new Dimension(8));
		layoutPrefab.setAlignment(EdgeAlignment.CENTER);
		Entity layout = nodeRoot.addChild(layoutPrefab);
		
		TextButtonPrefab hostButtonPrefab = new TextButtonPrefab("Create chat");
		hostButtonPrefab.setClickCallback((button) -> {
			chatApp.loadScene(chatApp.serverStartupScene);
		});
		layout.addChild(hostButtonPrefab);
		
		TextButtonPrefab connectButtonPrefab = new TextButtonPrefab("Join chat");
		connectButtonPrefab.setClickCallback((button) -> {
			chatApp.loadScene(chatApp.connectScene);
		});
		layout.addChild(connectButtonPrefab);
	}
}
