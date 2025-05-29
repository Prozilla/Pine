package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.system.ResourceUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class AudioSourcePool extends AssetPool<AudioSource> {
	
	@Override
	public AudioSource load(String path) {
		return super.load(path);
	}
	
	@Override
	protected AudioSource createAsset(String path) {
		ShortBuffer audioBuffer;
		int channels, sampleRate;
		
		try (MemoryStack stack = stackPush()) {
			//Allocate space to store return information from the function
			IntBuffer channelsBuffer   = stack.mallocInt(1);
			IntBuffer sampleRateBuffer = stack.mallocInt(1);
			
			String filePath = ResourceUtils.getResourcePath(path);
			
			audioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
			
			if (audioBuffer == null) {
				// Audio failed to load
				return fail(path, "Unknown error", null);
			}
			
			// Retrieve the extra information that was stored in the buffers by the function
			channels = channelsBuffer.get(0);
			sampleRate = sampleRateBuffer.get(0);
		}
		
		return new AudioSource(path, audioBuffer, channels, sampleRate);
	}
	
}
