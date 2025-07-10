package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.examples.chat.Chat;
import dev.prozilla.pine.examples.chat.entity.ButtonPrefab;

public class MenuScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setGap(new Dimension(16));
		layoutPrefab.setAlignment(EdgeAlignment.CENTER);
		layoutPrefab.setBorderImage(AssetPools.textures.load("images/png/Default/Panel/panel-031.png"), new Vector4f(0.3f, 0.3f, 0.3f, 0.3f), true);
		layoutPrefab.setBorder(new Dimension(16));
		layoutPrefab.setBorderColor(Chat.BACKGROUND_COLOR_B);
		layoutPrefab.setPadding(new DualDimension(64, 32));
		Entity layout = nodeRoot.addChild(layoutPrefab);
		
		ButtonPrefab hostButtonPrefab = new ButtonPrefab("Create chat");
		hostButtonPrefab.setClickCallback((button) -> {
			chatApp.loadScene(chatApp.serverStartupScene);
		});
		layout.addChild(hostButtonPrefab);
		
		ButtonPrefab connectButtonPrefab = new ButtonPrefab("Join chat");
		connectButtonPrefab.setClickCallback((button) -> {
			chatApp.loadScene(chatApp.connectScene);
		});
		layout.addChild(connectButtonPrefab);
	}
}
