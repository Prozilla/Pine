package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.examples.chat.server.Server;

public class CreateServerScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setDirection(Direction.RIGHT);
		layoutPrefab.setGap(new Dimension(8));
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextInputPrefab inputPrefab = new TextInputPrefab(String.valueOf(Server.DEFAULT_PORT));
		inputPrefab.setType(TextInputNode.Type.NUMBER);
		TextNode textNode = layoutNode.addChild(inputPrefab).getComponent(TextNode.class);
		
		TextButtonPrefab buttonPrefab = new TextButtonPrefab("Start");
		buttonPrefab.setClickCallback((button) -> {
			chatApp.startServer(Integer.parseInt(textNode.text));
		});
		layoutNode.addChild(buttonPrefab);
	}
	
}
