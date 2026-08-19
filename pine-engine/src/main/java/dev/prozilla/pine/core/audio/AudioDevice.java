package dev.prozilla.pine.core.audio;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.lwjgl.ALUtils;
import dev.prozilla.pine.core.Application;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.openal.AL10.AL_VERSION;
import static org.lwjgl.openal.AL10.alGetString;
import static org.lwjgl.openal.ALC10.*;

/**
 * Represents an OpenAL audio device.
 */
public class AudioDevice implements Initializable, Destructible {
	
	private final Application application;
	private final Logger logger;
	
	private boolean isInitialized;
	private long device;
	private long context;
	
	private static AudioDevice currentDevice;
	
	public AudioDevice(Application application) {
		this.application = application;
		logger = application.getLogger();
		isInitialized = false;
	}
	
	@Override
	public void init() {
		if (isInitialized) {
			return;
		}
		
		//Initialization
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);
		if (device == MemoryUtil.NULL) {
			handleError("Failed to open audio device");
			return;
		}

		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);

		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		if (context == MemoryUtil.NULL) {
			handleError("Failed to create OpenAL context");
			return;
		}

		alcMakeContextCurrent(context);
		int error = alcGetError(device);
		if (error != ALC_NO_ERROR) {
			handleError("Failed to make OpenAL context current", error);
			return;
		}

		ALCapabilities alCapabilities = AL.createCapabilities(deviceCapabilities);
		if (!alCapabilities.OpenAL10) {
			handleError("OpenAL 1.0 not supported by device");
			return;
		}

		logger.log("Audio device initialized");

		isInitialized = true;
		currentDevice = this;
	}
	
	@Override
	public void destroy() {
		if (context != MemoryUtil.NULL) {
			alcDestroyContext(context);
			context = MemoryUtil.NULL;
		}
		if (device != MemoryUtil.NULL) {
			alcCloseDevice(device);
			device = MemoryUtil.NULL;
		}
		if (currentDevice == this) {
			currentDevice = null;
		}
		isInitialized = false;
	}
	
	/**
	 * Checks if the device is ready to be used.
	 */
	public boolean isAvailable() {
		return isInitialized;
	}
	
	/**
	 * Checks if an audio device is currently available.
	 */
	public static boolean isAudioAvailable() {
		return currentDevice != null && currentDevice.isAvailable();
	}
	
	private void handleError(String message) {
		handleError(message, alcGetError(device));
	}
	
	private void handleError(String message, int error) {
		application.logLibraryError("OpenAL", error, String.format("%s: %s", ALUtils.getErrorString(error), message));
		destroy();
	}
	
	/**
	 * Returns the version of OpenAL.
	 * @return The OpenAL version
	 */
	public String getALVersion() {
		if (device == 0L) {
			return null;
		}
		return alGetString(AL_VERSION);
	}
	
}
