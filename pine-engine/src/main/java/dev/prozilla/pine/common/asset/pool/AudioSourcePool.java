package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.ResourceUtils;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
		String extension = PathUtils.getFileExtension(path);
		return switch (extension) {
			case "ogg" -> loadOgg(path);
			case "wav" -> loadWav(path);
			default -> fail(path, "Unknown file extension: " + extension);
		};
	}
	
	private AudioSource loadOgg(String path) {
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
	
	private AudioSource loadWav(String path) {
		try (InputStream inputStream = ResourceUtils.getResourceStream(path)) {
			byte[] bytes = inputStream.readAllBytes();
			ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.LITTLE_ENDIAN);
			buffer.put(bytes);
			buffer.flip();
			
			// Check the RIFF header
			if (buffer.getInt() != 0x46464952) { // "RIFF"
				return fail(path, "Invalid WAV file: missing RIFF header");
			}
			buffer.getInt(); // Skip chunk size
			if (buffer.getInt() != 0x45564157) { // "WAVE"
				return fail(path, "Invalid WAV file: missing WAVE header");
			}
			
			// Parse chunks
			int channels = 0;
			int sampleRate = 0;
			ShortBuffer pcm = null;
			
			while (buffer.remaining() >= 8) {
				int chunkId = buffer.getInt();
				int chunkSize = buffer.getInt();
				
				if (chunkId == 0x20746D66) { // "fmt "
					int audioFormat = buffer.getShort() & 0xFFFF;
					channels = buffer.getShort();
					sampleRate = buffer.getInt();
					buffer.position(buffer.position() + 6); // Skip byteRate + blockAlign
					int bitsPerSample = buffer.getShort();
					if (audioFormat != 1 || bitsPerSample != 16) {
						return fail(path, "Only 16-bit PCM WAV files are supported");
					}
					buffer.position(buffer.position() + (chunkSize - 16)); // Skip the rest of fmt
				} else if (chunkId == 0x61746164) { // "data"
					ByteBuffer data = buffer.slice();
					data.limit(chunkSize);
					pcm = data.slice().asShortBuffer();
					break;
				} else {
					// Unknown or unhandled chunk — skip it
					buffer.position(buffer.position() + chunkSize);
				}
			}
			
			if (pcm == null) {
				return fail(path, "WAV file missing data chunk");
			}
			
			return new AudioSource(path, pcm, channels, sampleRate);
		} catch (Exception e) {
			return fail(path, null, e);
		}
	}
	
}
