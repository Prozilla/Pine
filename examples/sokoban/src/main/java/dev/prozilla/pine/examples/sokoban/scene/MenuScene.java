package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Alignment;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.entity.ui.ButtonPrefab;

public class MenuScene extends Scene {

	@Override
	protected void load() {
		super.load();

		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);

		Entity nodeRoot = addEntity(new NodeRootPrefab());

		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(Anchor.CENTER);
		layoutPrefab.setDirection(Direction.DOWN);
		layoutPrefab.setAlignment(Alignment.CENTER);
		layoutPrefab.setGap(new Dimension(16));
		Entity layout = nodeRoot.addChild(layoutPrefab);

		TextPrefab titlePrefab = new TextPrefab("Sokoban");
		titlePrefab.setAnchor(Anchor.CENTER);
		titlePrefab.setFont(GameManager.instance.font.setSize(32));
		layout.addChild(titlePrefab);

		ButtonPrefab singlePlayerButtonPrefab = new ButtonPrefab("Singleplayer");
		singlePlayerButtonPrefab.setClickCallback((button) -> {
			getApplication().loadScene(new GameScene());
		});
		layout.addChild(singlePlayerButtonPrefab);

		ButtonPrefab hostButtonPrefab = new ButtonPrefab("Host Game");
		hostButtonPrefab.setClickCallback((button) -> {
			getApplication().loadScene(new HostGameScene());
		});
		layout.addChild(hostButtonPrefab);

		ButtonPrefab joinButtonPrefab = new ButtonPrefab("Join Game");
		joinButtonPrefab.setClickCallback((button) -> {
			getApplication().loadScene(new JoinGameScene());
		});
		layout.addChild(joinButtonPrefab);
	}

}
