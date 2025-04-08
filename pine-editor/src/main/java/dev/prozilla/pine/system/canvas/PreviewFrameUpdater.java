package dev.prozilla.pine.system.canvas;

import dev.prozilla.pine.component.PreviewData;
import dev.prozilla.pine.core.component.ui.FrameNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.entity.EntityTag;

public class PreviewFrameUpdater extends UpdateSystem {
	
	public PreviewFrameUpdater() {
		super(FrameNode.class, PreviewData.class);
		setRequiredTag(EntityTag.PREVIEW_FRAME_TAG);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		FrameNode frame = chunk.getComponent(FrameNode.class);
		PreviewData previewData = chunk.getComponent(PreviewData.class);
		
		frame.fbo = previewData.preview.getRenderer().getFbo();
	}
}
