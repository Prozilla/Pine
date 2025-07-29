package dev.prozilla.pine.core.system.standard.audio;

import dev.prozilla.pine.core.component.audio.AudioPlayer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class AudioPlayerInitializer extends InitSystem {
	
	public AudioPlayerInitializer() {
		super(AudioPlayer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		AudioPlayer audioPlayer = chunk.getComponent(AudioPlayer.class);
		
		if (audioPlayer.source != null) {
			audioPlayer.source.init();
		}
		
		if (audioPlayer.autoplay) {
			audioPlayer.play();
		}
	}
}
