package dev.prozilla.pine.common.system.resource.audio;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.resource.Resource;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class AudioSource implements Lifecycle, Resource {
	
	private final ShortBuffer rawAudioBuffer;
	private final int channels;
	private final int sampleRate;
	
	private int bufferPointer;
	private int sourcePointer;
	
	private boolean initialized;
	private boolean isPlaying;
	
	private final String path;
	
	public AudioSource(String path, ShortBuffer buffer, int channels, int sampleRate) {
		this.path = path;
		this.rawAudioBuffer = buffer;
		this.channels = channels;
		this.sampleRate = sampleRate;
		
		initialized = false;
		isPlaying = false;
	}
	
	@Override
	public void init() {
		if (initialized) {
			throw new IllegalStateException("Audio source has already been initialized");
		}
		
		//Find the correct OpenAL format
		int format = -1;
		if (channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL_FORMAT_STEREO16;
		}
		
		//Request space for the buffer
		bufferPointer = alGenBuffers();
		
		//Send the data to OpenAL
		alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
		
		//Free the memory allocated by STB
		free(rawAudioBuffer);
		
		//Request a source
		sourcePointer = alGenSources();
		
		//Assign the sound we just loaded to the source
		alSourcei(sourcePointer, AL_BUFFER, bufferPointer);
		
		initialized = true;
	}
	
	@Override
	public void destroy() {
		if (initialized) {
			alDeleteSources(sourcePointer);
			alDeleteBuffers(bufferPointer);
		}
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	public void play() {
		if (!initialized) {
			init();
		}
		
		alSourcePlay(sourcePointer);
		isPlaying = true;
	}
	
	public void pause() {
		if (!isPlaying) {
			return;
		}
		
		alSourcePause(sourcePointer);
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setVolume(float volume) {
		setAttribute(AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		setAttribute(AL_PITCH, pitch);
	}
	
	public void setLoop(boolean loop) {
		setAttribute(AL_LOOPING, loop);
	}
	
	public void setAttribute(int attribute, float value) {
		requireInitialized();
		alSourcef(sourcePointer, attribute, value);
	}
	
	public void setAttribute(int attribute, boolean value) {
		setAttribute(attribute, value ? AL_TRUE : AL_FALSE);
	}
	
	public void setAttribute(int attribute, int value) {
		requireInitialized();
		alSourcei(sourcePointer, attribute, value);
	}
	
	private void requireInitialized() {
		if (!initialized) {
			throw new IllegalStateException("Audio source has not been initialized yet");
		}
	}
	
}
