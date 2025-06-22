package dev.prozilla.pine.common.asset.audio;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SAMPLE_OFFSET;

public class AudioSource implements Initializable, Destructible, Asset, AudioSourceContext {
	
	private final ShortBuffer rawAudioBuffer;
	private final int channels;
	private final int sampleRate;
	private short[] samples;
	
	private int bufferPointer;
	private int sourcePointer;
	
	private boolean initialized;
	private boolean isPlaying;
	private boolean capture;
	
	private final String path;
	
	public AudioSource(String path, ShortBuffer buffer, int channels, int sampleRate) {
		this.path = path;
		this.rawAudioBuffer = buffer;
		this.channels = channels;
		this.sampleRate = sampleRate;
		
		initialized = false;
		isPlaying = false;
		capture = false;
		
		samples = null;
	}
	
	@Override
	public void init() throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("Audio source has already been initialized");
		}
		
		// Capture samples
		if (capture) {
			samples = new short[rawAudioBuffer.remaining()];
			rawAudioBuffer.get(samples);
			rawAudioBuffer.rewind();
		}

		// Find the correct OpenAL format
		int format = -1;
		if (channels == 1) {
			format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL_FORMAT_STEREO16;
		}
		
		// Send audio data to OpenAL via a buffer
		bufferPointer = alGenBuffers();
		alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
//		free(rawAudioBuffer);
		
		// Create a source
		sourcePointer = alGenSources();
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
	
	public void setCapture(boolean capture) {
		this.capture = capture;
	}
	
	public boolean isCaptured() {
		return samples != null;
	}
	
	@Override
	public void play() {
		if (isPlaying) {
			return;
		}
		
		if (!initialized) {
			init();
		}
		
		alSourcePlay(sourcePointer);
		isPlaying = true;
	}
	
	@Override
	public void pause() {
		if (!isPlaying) {
			return;
		}
		
		alSourcePause(sourcePointer);
		isPlaying = false;
	}
	
	@Override
	public boolean isPlaying() {
		return isPlaying;
	}
	
	@Override
	public void setVolume(float volume) throws IllegalStateException {
		setAttribute(AL_GAIN, volume);
	}
	
	@Override
	public void setPitch(float pitch) throws IllegalStateException {
		setAttribute(AL_PITCH, pitch);
	}
	
	@Override
	public void setLoop(boolean loop) throws IllegalStateException {
		setAttribute(AL_LOOPING, loop);
	}
	
	public void setAttribute(int attribute, float value) throws IllegalStateException {
		requireInitialized();
		alSourcef(sourcePointer, attribute, value);
	}
	
	public void setAttribute(int attribute, boolean value) throws IllegalStateException {
		setAttribute(attribute, value ? AL_TRUE : AL_FALSE);
	}
	
	public void setAttribute(int attribute, int value) throws IllegalStateException {
		requireInitialized();
		alSourcei(sourcePointer, attribute, value);
	}
	
	public double[] getAverageMagnitudes(int intervalCount) {
		double[] magnitudes = getMagnitudes();
		if (magnitudes == null) {
			return null;
		}
		
		double[] averageMagnitudes = new double[intervalCount];
		int intervalSize = magnitudes.length / intervalCount;
		for (int i = 0; i < intervalCount; i++) {
			int intervalStart = i * intervalSize;
			int intervalEnd = Math.min(intervalStart + intervalSize, magnitudes.length);
			double sum = 0;
			for (int j = intervalStart; j < intervalEnd; j++) {
				sum += magnitudes[j];
			}
			averageMagnitudes[i] = sum / (intervalEnd - intervalStart);
		}
		
		return averageMagnitudes;
	}
	
	public double[] getMagnitudes() {
		requireInitialized();
		if (samples == null) {
			return null;
		}
		
		int windowSize = 1024;
		
		int frameOffset = alGetSourcei(sourcePointer, AL_SAMPLE_OFFSET);
		int sampleOffset = frameOffset * channels;
		
		// Clamp offset to avoid overflow
		if (sampleOffset + windowSize >= samples.length) {
			sampleOffset = samples.length - windowSize - 1;
		}
		
		double[] real = new double[windowSize];
		double[] imag = new double[windowSize];
		
		for (int i = 0; i < windowSize; i++) {
			real[i] = samples[sampleOffset + i] / 32768.0;
			imag[i] = 0.0;
		}
		
		FastFourierTransform.fft(real, imag);
		return FastFourierTransform.magnitude(real, imag);
	}
	
	private void requireInitialized() throws IllegalStateException {
		if (!initialized) {
			throw new IllegalStateException("Audio source has not been initialized yet");
		}
	}
	
}
