package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.util.ArrayUtils;
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
		if (magnitudes != null) {
			ArrayUtils.shuffle(magnitudes, Main.SHUFFLE_SEED);
		}
		
		forEach((chunk) -> {
			BarData barData = chunk.getComponent(BarData.class);
			SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
			
			float factor = 0.1f;
			if (magnitudes != null && source.isPlaying()) {
				double magnitude = magnitudes[barData.index];
				factor += (float)Math.log1p(magnitude * 10) * (0.25f + 0.75f * (float)Math.sin((float)barData.index / (Main.BAR_COUNT - 1) * Math.PI));
			}
			
			spriteRenderer.scale.y = MathUtils.lerp(spriteRenderer.scale.y, factor, deltaTime * Main.LERP_SPEED);
		});
	}
}
