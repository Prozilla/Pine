package dev.prozilla.pine.core.audio;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.openal.ALUtils;
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
	
	private final Logger logger;
	
	private boolean isInitialized;
	private long device;
	private long context;
	
	public AudioDevice(Application application) {
		logger = application.getLogger();
		isInitialized = false;
	}
	
	@Override
	public void init() {
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
	}
	
	@Override
	public void destroy() {
		if (context != 0L) {
			alcDestroyContext(context);
			context = 0L;
		}
		if (device != 0L) {
			alcCloseDevice(device);
			device = 0L;
		}
	}
	
	/**
	 * Checks if the device is ready to be used.
	 */
	public boolean isAvailable() {
		return isInitialized;
	}
	
	private void handleError(String message) {
		handleError(message, alcGetError(device));
	}
	
	private void handleError(String message, int error) {
		logger.error(String.format("%s: %s - %s", message, error, ALUtils.getErrorString(error)));
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
