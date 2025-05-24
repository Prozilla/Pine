package dev.prozilla.pine.entity.canvas;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.component.PreviewData;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.FramePrefab;
import dev.prozilla.pine.entity.EntityTag;

public class PreviewFramePrefab extends FramePrefab {
	
	protected Application preview;
	
	public PreviewFramePrefab(Application preview) {
		super();
		setName("PreviewFrame");
		setTag(EntityTag.PREVIEW_FRAME_TAG);
		setAnchor(GridAlignment.CENTER);
		setBackgroundColor(new Color(0.1f, 0.1f, 0.1f));
		
		this.preview = preview;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new PreviewData(preview));
	}
}
