package dev.prozilla.pine.common.logging;

import org.gradle.internal.impldep.javax.annotation.Nonnull;

public interface LogLayer {
	
	/**
	 * Logs an empty line.
	 */
	default void log() {
		log("");
	}
	
	default void log(boolean x) {
		log(String.valueOf(x));
	}
	
	default void log(char x) {
		log(String.valueOf(x));
	}
	
	default void log(int x) {
		log(String.valueOf(x));
	}
	
	default void log(long x) {
		log(String.valueOf(x));
	}
	
	default void log(float x) {
		log(String.valueOf(x));
	}
	
	default void log(double x) {
		log(String.valueOf(x));
	}
	
	default void log(@Nonnull char[] x) {
		log(String.valueOf(x));
	}
	
	default void log(Object x) {
		log(String.valueOf(x));
	}
	
	/**
	 * Logs a formatted string of text.
	 */
	default void logf(@Nonnull String format, Object... args) {
		log(format.formatted(args));
	}
	
	/**
	 * Logs a string of text.
	 */
	void log(String text);
	
}
