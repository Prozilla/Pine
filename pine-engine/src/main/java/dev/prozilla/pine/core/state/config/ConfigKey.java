package dev.prozilla.pine.core.state.config;

/**
 * Represents a key used to identify a configuration option.
 * @param <T> Type of the associated option
 * @see Config
 */
public record ConfigKey<T>(String key, Class<T> type) {

}
