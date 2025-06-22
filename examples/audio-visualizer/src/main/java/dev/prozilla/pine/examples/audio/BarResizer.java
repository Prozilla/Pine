package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

public class BarResizer extends UpdateSystemBase {
	
	private final AudioSource source;
	
	public BarResizer(AudioSource source) {
		super(BarData.class, SpriteRenderer.class);
		this.source = source;
	}
	
	@Override
	public void update(float deltaTime) {
		double[] magnitudes = source.getAverageMagnitudes(Main.BAR_COUNT);
		
		forEach((chunk) -> {
			Transform transform = chunk.getTransform();
			BarData barData = chunk.getComponent(BarData.class);
			SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
			
			float y = (spriteRenderer.getHeight() - Main.HEIGHT) / 2f;
			
			if (magnitudes != null && source.isPlaying()) {
				double magnitude = magnitudes[barData.index];
				y += (float)Math.log1p(magnitude * 100) * 20f;
			}
			
			transform.position.y = MathUtils.lerp(transform.position.y, y, deltaTime * Main.LERP_SPEED);
		});
	}
}
