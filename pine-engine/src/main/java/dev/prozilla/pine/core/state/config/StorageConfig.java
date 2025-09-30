package dev.prozilla.pine.core.state.config;

import java.util.Objects;

/**
 * Manages configuration options related to storage.
 */
public class StorageConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> ENABLE_LOCAL_STORAGE = new ConfigKey<>("enableLocalStorage", Boolean.class);
	public static final ConfigKey<Boolean> LOAD_ON_READ = new ConfigKey<>("loadOnRead", Boolean.class);
	public static final ConfigKey<Boolean> SAVE_ON_WRITE = new ConfigKey<>("saveOnWrite", Boolean.class);
	
	// Predefines options
	public final ConfigOption<Boolean> enableLocalStorage = new ConfigOption<>(true, Objects::nonNull);
	public final ConfigOption<Boolean> loadOnRead = new ConfigOption<>(true, Objects::nonNull);
	public final ConfigOption<Boolean> saveOnWrite = new ConfigOption<>(true, Objects::nonNull);
	
}
