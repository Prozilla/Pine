package dev.prozilla.pine.entity.canvas.header;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.ContainerPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;

public class HeaderPrefab extends ContainerPrefab {
	
	public HeaderPrefab() {
		super();
		setName("Header");
		setAnchor(RectTransform.Anchor.TOP_LEFT);
		setSize(new DualDimension(Dimension.viewportWidth(), new Dimension(32)));
		setPadding(new DualDimension(8));
		setBackgroundColor(new Color(0.1f, 0.1f, 0.1f));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addChild(new TextPrefab("Pine", Color.WHITE));
	}
}
