package dev.prozilla.pine.core.component.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AudioEffectPlayer extends Component implements Initializable {
	
	public final List<AudioSource> sources;
	private final int initialSourcesCount;
	public int maxSources;
	
	public boolean autoplay;
	private VariableProperty<Float> volume;
	private VariableProperty<Float> pitch;
	private VariableProperty<Float> gain;
	
	private boolean isMuted;
	
	private static final Random random = new Random();
	
	/** Determines how many clones can exist per source by default. The default maximum amount of sources is calculated by multiplying the amount of unique sources by this value plus one. */
	public static final int DEFAULT_MAX_SOURCE_CLONES = 3;
	
	public AudioEffectPlayer(List<AudioSource> sources) {
		this(sources, AudioPlayer.DEFAULT_VOLUME);
	}
	
	public AudioEffectPlayer(List<AudioSource> sources, float volume) {
		this.sources = new ArrayList<>(sources);
		initialSourcesCount = sources.size();
		maxSources = initialSourcesCount * (DEFAULT_MAX_SOURCE_CLONES + 1);
		
		setVolume(volume);
		
		autoplay = false;
		isMuted = false;
	}
	
	@Override
	public void init() {
		prepareSources(sources.size());
	}
	
	public void playRandom() {
		playRandom(0, initialSourcesCount - 1);
	}
	
	public void playRandom(int minIndex, int maxIndex) {
		play(random.nextInt(minIndex, maxIndex + 1));
	}
	
	public void play(int index) {
		AudioSource source = getSource(index);
		source.init();
		source.setVolume(isMuted ? 0 : getVolume());
		source.setPitch(getPitch());
		source.setGain(getGain());
		source.play();
	}
	
	public void mute() {
		isMuted = true;
		for (AudioSource source : sources) {
			source.init();
			source.setVolume(0);
		}
	}
	
	public void unmute() {
		isMuted = false;
		for (AudioSource source : sources) {
			source.init();
			source.setVolume(getVolume());
		}
	}
	
	public boolean isMuted() {
		return isMuted;
	}
	
	public void setGlobalVolume(float volume) {
		if (volume == AudioPlayer.DEFAULT_VOLUME) {
			setGlobalVolume(null);
		} else {
			setGlobalVolume(new FixedProperty<>(volume));
		}
	}
	
	public void setGlobalVolume(VariableProperty<Float> volume) {
		if (Objects.equals(this.volume, volume)) {
			return;
		}
		
		setVolume(volume);
		for (AudioSource source : sources) {
			source.init();
			source.setVolume(getVolume());
		}
	}
	
	public void setVolume(float volume) {
		if (volume == AudioPlayer.DEFAULT_VOLUME) {
			setVolume(null);
		} else {
			setVolume(new FixedProperty<>(volume));
		}
	}
	
	public void setVolume(VariableProperty<Float> volume) {
		if (Objects.equals(this.volume, volume)) {
			return;
		}
		
		this.volume = volume;
	}
	
	private float getVolume() {
		return VariableProperty.getValue(volume, AudioPlayer.DEFAULT_VOLUME);
	}
	
	public void setGlobalPitch(float pitch) {
		if (pitch == AudioPlayer.DEFAULT_PITCH) {
			setGlobalPitch(null);
		} else {
			setGlobalPitch(new FixedProperty<>(pitch));
		}
	}
	
	public void setGlobalPitch(VariableProperty<Float> pitch) {
		if (Objects.equals(this.pitch, pitch)) {
			return;
		}
		
		setPitch(pitch);
		for (AudioSource source : sources) {
			source.init();
			source.setPitch(getPitch());
		}
	}
	
	public void setPitch(float pitch) {
		if (pitch == AudioPlayer.DEFAULT_PITCH) {
			setPitch(null);
		} else {
			setPitch(new FixedProperty<>(pitch));
		}
	}
	
	public void setPitch(VariableProperty<Float> pitch) {
		if (Objects.equals(this.pitch, pitch)) {
			return;
		}
		
		this.pitch = pitch;
	}
	
	private float getPitch() {
		return VariableProperty.getValue(pitch, AudioPlayer.DEFAULT_PITCH);
	}
	
	public void setGlobalGain(float gain) {
		if (gain == AudioPlayer.DEFAULT_GAIN) {
			setGlobalGain(null);
		} else {
			setGlobalGain(new FixedProperty<>(gain));
		}
	}
	
	public void setGlobalGain(VariableProperty<Float> gain) {
		if (Objects.equals(this.gain, gain)) {
			return;
		}
		
		setGain(gain);
		for (AudioSource source : sources) {
			source.init();
			source.setGain(getGain());
		}
	}
	
	public void setGain(float gain) {
		if (gain == AudioPlayer.DEFAULT_GAIN) {
			setGain(null);
		} else {
			setGain(new FixedProperty<>(gain));
		}
	}
	
	public void setGain(VariableProperty<Float> gain) {
		if (Objects.equals(this.gain, gain)) {
			return;
		}
		
		this.gain = gain;
	}
	
	private float getGain() {
		return VariableProperty.getValue(gain, AudioPlayer.DEFAULT_GAIN);
	}
	
	public void setMaxSources(int maxSources) {
		if (this.maxSources == maxSources) {
			return;
		}
		
		this.maxSources = maxSources;
	}
	
	public void prepareSources(int count) {
		count = MathUtils.clamp(count, 0, maxSources);
		
		if (count > initialSourcesCount) {
			for (int i = 0; i < count - initialSourcesCount; i++) {
				AudioSource source = sources.get(i % initialSourcesCount).clone();
				sources.add(source);
			}
		}
		
		for (int i = 0; i < count; i++) {
			sources.get(i).init();
		}
	}
	
	private AudioSource getSource(int index) {
		index = index % initialSourcesCount;
		AudioSource source = null;
		
		while (index < sources.size() && source == null) {
			source = sources.get(index);
			if (source.isPlaying()) {
				source = null;
				index += initialSourcesCount;
			}
		}
		
		if (source == null) {
			prepareSources(index + 1);
			return sources.get(index);
		} else {
			return source;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		for (AudioSource source : sources) {
			source.destroy();
		}
	}
	
}
