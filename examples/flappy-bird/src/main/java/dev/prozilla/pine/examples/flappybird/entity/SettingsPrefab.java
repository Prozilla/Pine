package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.flappybird.EntityTag;
import dev.prozilla.pine.examples.flappybird.GameManager;
import dev.prozilla.pine.examples.flappybird.component.SettingsData;

public class SettingsPrefab extends LayoutPrefab {
	
	public SettingsPrefab(Font font) {
		setBackgroundColor(Color.white());
		setSize(DualDimension.fullscreen());
		setAlignment(EdgeAlignment.CENTER);
		setDistribution(LayoutNode.Distribution.CENTER);
		setAbsolutePosition(true);
		setAnchor(GridAlignment.CENTER);
		setGap(new Dimension(16));
		setActive(false);
		setTag(EntityTag.SETTINGS);
		
		TextPrefab titlePrefab = new TextPrefab("Options", Color.black());
		titlePrefab.setFont(font);
		
		LayoutPrefab seed = new LayoutPrefab();
		seed.setDirection(Direction.RIGHT);
		seed.setGap(new Dimension(16));
		seed.setAlignment(EdgeAlignment.CENTER);
		
		TextPrefab seedLabel = new TextPrefab("Seed", Color.black());
		seedLabel.setFont(font.setSize(24));
		
		TextInputPrefab seedInput = new TextInputPrefab(String.valueOf(GameManager.instance.seed));
		seedInput.setFont(font.setSize(24));
		seedInput.setColor(Color.white());
		seedInput.setBackgroundColor(Color.black());
		seedInput.setPadding(new DualDimension(16, 8));
		seedInput.setType(TextInputNode.Type.NUMBER);
		
		seed.addChildren(seedLabel, seedInput);
		
		TextButtonPrefab backButton = new TextButtonPrefab("Back");
		backButton.setColor(Color.white());
		backButton.setBackgroundColor(Color.black());
		backButton.setFont(font.setSize(24));
		backButton.setPadding(new DualDimension(16, 8));
		backButton.setClickCallback((entity) -> entity.getParentWithTag(EntityTag.SETTINGS).setActive(false));
		
		addChildren( backButton, seed, titlePrefab);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextInputNode seedInput = entity.getChild(1).getLastChild().getComponent(TextInputNode.class);
		entity.addComponent(new SettingsData(seedInput));
	}
	
}
