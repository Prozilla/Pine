package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.core.state.config.option.BooleanConfigOption;

/**
 * Manages configuration options related to storage.
 */
public class StorageConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> ENABLE_LOCAL_STORAGE = new ConfigKey<>("enableLocalStorage", Boolean.class);
	public static final ConfigKey<Boolean> LOAD_ON_READ = new ConfigKey<>("loadOnRead", Boolean.class);
	public static final ConfigKey<Boolean> SAVE_ON_WRITE = new ConfigKey<>("saveOnWrite", Boolean.class);
	
	// Predefines options
	/** Determines whether the local storage can use a file to persist data across sessions. Defaults to {@code false}. */
	public final BooleanConfigOption enableLocalStorage = new BooleanConfigOption(false);
	/** Determines whether the local storage will be loaded every time it is read from. Defaults to {@code false}. */
	public final BooleanConfigOption loadOnRead = new BooleanConfigOption(false);
	/** Determines whether the local storage will be saved every time it is written to. Defaults to {@code false}. */
	public final BooleanConfigOption saveOnWrite = new BooleanConfigOption(false);
	
}
