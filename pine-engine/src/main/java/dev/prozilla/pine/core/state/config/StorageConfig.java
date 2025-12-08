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
	public final BooleanConfigOption enableLocalStorage = new BooleanConfigOption(false);
	public final BooleanConfigOption loadOnRead = new BooleanConfigOption(false);
	public final BooleanConfigOption saveOnWrite = new BooleanConfigOption(false);
	
}
