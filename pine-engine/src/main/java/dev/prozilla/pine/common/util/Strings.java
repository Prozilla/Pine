package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.exception.InvalidStringException;
import org.gradle.internal.impldep.javax.annotation.Nonnull;

/**
 * Static utility methods for checking certain conditions before operation on strings.
 * @deprecated Replaced by {@link Checks} as of 1.2.0
 */
@Deprecated
public final class Strings {
	
	public static String requireNonBlank(@Nonnull String string) throws InvalidStringException {
		return requireNonBlank(string, null);
	}
	
	public static String requireNonBlank(@Nonnull String string, String message) throws InvalidStringException {
		if (string.isBlank()) {
			throw new InvalidStringException(message != null ? message : "string must not be blank");
		} else {
			return string;
		}
	}
	
	public static String requireNonEmpty(@Nonnull String string) throws InvalidStringException {
		return requireNonEmpty(string, null);
	}
	
	public static String requireNonEmpty(@Nonnull String string, String message) throws InvalidStringException {
		if (string.isEmpty()) {
			throw new InvalidStringException(message != null ? message : "string must not be empty");
		} else {
			return string;
		}
	}
	
	public static String requirePrefix(@Nonnull String string, @Nonnull String prefix) throws InvalidStringException {
		return requirePrefix(string, prefix, null);
	}
	
	public static String requirePrefix(@Nonnull String string, @Nonnull String prefix, String message) throws InvalidStringException {
		if (!string.startsWith(prefix)) {
			throw new InvalidStringException(message != null ? message : "string must start with prefix: " + prefix);
		} else {
			return string;
		}
	}
	
	public static String requireSuffix(@Nonnull String string, @Nonnull String suffix) throws InvalidStringException {
		return requireSuffix(string, suffix, null);
	}
	
	public static String requireSuffix(@Nonnull String string, @Nonnull String suffix, String message) throws InvalidStringException {
		if (!string.endsWith(suffix)) {
			throw new InvalidStringException(message != null ? message : "string must end with suffix: " + suffix);
		} else {
			return string;
		}
	}
}
