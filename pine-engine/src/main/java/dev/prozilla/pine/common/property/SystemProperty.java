package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.checks.Checks;

/**
 * Represents a system property whose value is read using {@link System#getProperty}.
 *
 * <p>Unless {@link #fetch} is explicitly called, the value will only be read once and cached for later uses.</p>
 */
public class SystemProperty extends LazyProperty<String> {
	
	private final String key;
	
	public SystemProperty(String key) {
		Checks.string(key, "key").isNotEmpty();
		this.key = key;
	}
	
	/**
	 * Reads the system property.
	 * @see System#getProperty
	 */
	@Override
	public String fetch() {
		value = System.getProperty(key);
		return value;
	}
	
}
