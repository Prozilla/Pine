package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.mutable.MutableStringProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * Represents a system property whose value is read using {@link System#getProperty}.
 *
 * <p>Unless {@link #fetch} is explicitly called, the value will only be read once and cached for later uses.</p>
 */
public class SystemProperty extends LazyProperty<String> implements MutableStringProperty {
	
	private final String key;
	
	public SystemProperty(String key) {
		Checks.string(key, "key").isNotEmpty();
		this.key = key;
	}
	
	/**
	 * Reads the value of this system property.
	 * @see System#getProperty
	 */
	@Override
	public String fetch() {
		value = System.getProperty(key);
		return value;
	}
	
	/**
	 * Overrides the value of this property without modifying the actual system property.
	 * @param value The new value
	 */
	@Override
	public boolean setValue(String value) {
		if (Objects.equals(this.value, value)) {
			return false;
		}
		this.value = value;
		return true;
	}
	
}
