package dev.prozilla.pine.entity.canvas.header;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

public class HeaderPrefab extends LayoutPrefab {
	
	public HeaderPrefab() {
		super();
		setName("Header");
		setAnchor(GridAlignment.TOP_LEFT);
		setSize(new DualDimension(Dimension.viewportWidth(), new Dimension(32)));
		setPadding(new DualDimension(8));
		setBackgroundColor(new Color(0.1f, 0.1f, 0.1f));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addChild(new TextPrefab("Pine", Color.white()));
	}
}
