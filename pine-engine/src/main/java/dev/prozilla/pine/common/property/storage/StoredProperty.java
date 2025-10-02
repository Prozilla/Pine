package dev.prozilla.pine.common.property.storage;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.property.mutable.MutableProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.storage.Storage;

public abstract class StoredProperty<T> implements MutableProperty<T>, Destructible {

	protected final Storage storage;
	protected final String key;
	
	public StoredProperty(Storage storage, String key) {
		this.storage = Checks.isNotNull(storage, "storage");
		this.key = Checks.isNotNull(key, "key");
	}
	
	public boolean setValue(String value) {
		return storage.setItem(key, value);
	}
	
	@Override
	public boolean setValue(T value) {
		return storage.setItem(key, value);
	}
	
	/**
	 * Removes the corresponding item from the store.
	 */
	@Override
	public void destroy() {
		storage.removeItem(key);
	}
	
	/**
	 * Creates a stored string property with the same key as this property.
	 * @return A new stored string property.
	 * @see Storage#stringProperty(String)
	 */
	@Override
	public StringProperty toStringProperty() {
		return storage.stringProperty(key);
	}
	
}
