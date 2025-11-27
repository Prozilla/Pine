package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.storage.LocalStorage;
import dev.prozilla.pine.examples.flappybird.FlappyBird;

public class MenuPrefab extends NodeRootPrefab {
	
	protected Font font;
	
	public MenuPrefab(Font font) {
		this.font = font;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setDirection(Direction.DOWN);
		layoutPrefab.setAlignment(EdgeAlignment.CENTER);
		layoutPrefab.setGap(new Dimension(48));
		Entity layout = entity.addChild(layoutPrefab);
		
		// Title
		TextPrefab titlePrefab = new TextPrefab(FlappyBird.TITLE);
		titlePrefab.setFont(font.setSize(64));
		layout.addChild(titlePrefab);
		
		// Options
		Entity options = entity.addChild(new SettingsPrefab(font));
		
		// Buttons
		layoutPrefab.setGap(new Dimension(8));
		Entity buttonLayout = layout.addChild(layoutPrefab);
		
		TextButtonPrefab textButtonPrefab = new TextButtonPrefab();
		textButtonPrefab.setFont(font.setSize(24));
		textButtonPrefab.setPadding(new DualDimension(16, 4));
		
		// Play button
		textButtonPrefab.setText("Play");
		textButtonPrefab.setClickCallback((button) -> button.getApplication().loadScene(FlappyBird.gameScene));
		buttonLayout.addChild(textButtonPrefab);
		
		// Settings button
		textButtonPrefab.setText("Options");
		textButtonPrefab.setClickCallback((button) -> options.toggleActive());
		buttonLayout.addChild(textButtonPrefab);
		
		// Exit button
		textButtonPrefab.setText("Exit");
		textButtonPrefab.setClickCallback(ApplicationProvider::stopApplication);
		buttonLayout.addChild(textButtonPrefab);
		
		// High score
		TextPrefab textPrefab = new TextPrefab("Highscore: 0", Color.black());
		textPrefab.setFont(font.setSize(24));
		textPrefab.setAbsolutePosition(true);
		textPrefab.setAnchor(GridAlignment.BOTTOM_LEFT);
		textPrefab.setMargin(new DualDimension(16));
		LocalStorage localStorage = entity.getLocalStorage();
		textPrefab.setText(localStorage.stringProperty("highscore").replaceNull("0").prepend("Highscore: "));
		layout.addChild(textPrefab);
	}
}
