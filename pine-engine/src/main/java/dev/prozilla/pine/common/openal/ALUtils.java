package dev.prozilla.pine.common.openal;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.ALC10;

import static org.lwjgl.openal.AL10.*;

/**
 * OpenAL utilities.
 */
public final class ALUtils {
	
	private ALUtils() {}
	
	/**
	 * Translates an OpenAL error code to a string describing the error.
	 * Source: <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">OpenAL Programmer's Guide</a>.
	 * @param errorCode The error code, as returned by {@link ALC10#alcGetError}
	 * @return The error description.
	 */
	public static @NotNull String getErrorString(int errorCode) {
		return switch (errorCode) {
			case AL_NO_ERROR -> "No error";
			case AL_INVALID_NAME -> "A bad name (ID) was passed to an OpenAL function";
			case AL_INVALID_ENUM -> "An invalid enum value was passed to an OpenAL function";
			case AL_INVALID_VALUE -> "An invalid value was passed to an OpenAL function";
			case AL_INVALID_OPERATION -> "The requested operation is not valid";
			case AL_OUT_OF_MEMORY -> "The requested operation resulted in OpenAL running out of memory";
			default -> "Unknown error";
		};
	}
	
}
