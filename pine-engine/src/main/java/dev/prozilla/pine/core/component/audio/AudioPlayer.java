package dev.prozilla.pine.core.component.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.audio.AudioSourceContext;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

public class AudioPlayer extends Component implements AudioSourceContext {
	
	public AudioSource source;
	
	public boolean autoplay;
	private float volume;
	
	private boolean isMuted;
	
	public final static float DEFAULT_VOLUME = 1f;
	public final static float DEFAULT_PITCH = 1f;
	public final static float DEFAULT_GAIN = 1f;
	
	public AudioPlayer(String sourcePath) {
		this(sourcePath, DEFAULT_VOLUME);
	}
	
	public AudioPlayer(AudioSource source) {
		this(source, DEFAULT_VOLUME);
	}
	
	public AudioPlayer(String sourcePath, float volume) {
		this(AssetPools.audioSources.load(sourcePath), volume);
	}
	
	public AudioPlayer(AudioSource source, float volume) {
		this.source = Checks.isNotNull(source, "source");
		this.volume = DEFAULT_VOLUME;
		
		setVolume(volume);
		
		autoplay = false;
		isMuted = false;
	}
	
	@Override
	public void rewind() {
		source.rewind();
	}
	
	@Override
	public void play() {
		source.play();
	}
	
	@Override
	public void pause() {
		source.pause();
	}
	
	public void setSource(AudioSource source) {
		this.source = Checks.isNotNull(source, "source");
	}
	
	@Override
	public boolean isPlaying() {
		return source.isPlaying();
	}
	
	@Override
	public void setVolume(float volume) {
		if (this.volume == volume) {
			return;
		}
		
		this.volume = volume;
		source.init();
		source.setVolume(volume);
	}
	
	@Override
	public void setPitch(float pitch) {
		source.init();
		source.setPitch(pitch);
	}
	
	@Override
	public void setGain(float gain) {
		source.init();
		source.setGain(gain);
	}
	
	@Override
	public void setLoop(boolean loop) {
		source.init();
		source.setLoop(loop);
	}
	
	public void toggleMute() {
		if (isMuted) {
			unmute();
		} else {
			mute();
		}
	}
	
	public void mute() {
		source.init();
		source.setVolume(0);
		isMuted = true;
	}
	
	public void unmute() {
		source.init();
		source.setVolume(volume);
		isMuted = false;
	}
	
	public boolean isMuted() {
		return isMuted;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		source.destroy();
	}
	
}
