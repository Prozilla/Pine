package dev.prozilla.pine.common.asset.audio;

import dev.prozilla.pine.common.ContextOf;

@ContextOf(AudioSource.class)
public interface AudioSourceContext {
	
	default void togglePause() {
		if (isPlaying()) {
			pause();
		} else {
			play();
		}
	}
	
	void play();
	
	void pause();
	
	boolean isPlaying();
	
	void setVolume(float volume);
	
	void setPitch(float pitch);
	
	void setLoop(boolean loop);
	
}
