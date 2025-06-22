package dev.prozilla.pine.core.component.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.audio.AudioSourceContext;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.component.Component;

import java.util.Objects;

public class AudioPlayer extends Component implements AudioSourceContext {
	
	public AudioSource source;
	
	public boolean autoplay;
	private float volume;
	
	private boolean isMuted;
	
	public final static float DEFAULT_VOLUME = 1f;
	
	public AudioPlayer(String sourcePath) {
		this(AssetPools.audioSources.load(sourcePath));
	}
	
	public AudioPlayer(AudioSource source) {
		this(source, DEFAULT_VOLUME);
	}
	
	public AudioPlayer(AudioSource source, float volume) {
		this.source = Objects.requireNonNull(source, "source must not be null");
		this.volume = DEFAULT_VOLUME;
		
		setVolume(volume);
		
		autoplay = false;
		isMuted = false;
	}
	
	@Override
	public void rewind() {
		requireSource();
		source.rewind();
	}
	
	@Override
	public void play() {
		requireSource();
		source.play();
	}
	
	@Override
	public void pause() {
		requireSource();
		source.pause();
	}
	
	private void requireSource() {
		if (source == null) {
			throw new IllegalStateException("source must not be null");
		}
	}
	
	@Override
	public boolean isPlaying() {
		return source != null && source.isPlaying();
	}
	
	@Override
	public void setVolume(float volume) {
		if (this.volume == volume) {
			return;
		}
		
		this.volume = volume;
		if (source != null) {
			source.setVolume(volume);
		}
	}
	
	@Override
	public void setPitch(float pitch) {
		if (source != null) {
			source.setPitch(pitch);
		}
	}
	
	@Override
	public void setLoop(boolean loop) {
		if (source != null) {
			source.setLoop(loop);
		}
	}
	
	public void toggleMute() {
		if (isMuted) {
			unmute();
		} else {
			mute();
		}
	}
	
	public void mute() {
		source.setVolume(0);
		isMuted = true;
	}
	
	public void unmute() {
		source.setVolume(volume);
		isMuted = false;
	}
	
	public boolean isMuted() {
		return isMuted;
	}
}
