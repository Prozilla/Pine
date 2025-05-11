package dev.prozilla.pine.core.audio;

import dev.prozilla.pine.common.Lifecycle;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;

public class AudioDevice implements Lifecycle {
	
	private long device;
	private long context;
	
	@Override
	public void init() {
		//Initialization
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);
		
		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
	}
	
	@Override
	public void destroy() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
	
}
