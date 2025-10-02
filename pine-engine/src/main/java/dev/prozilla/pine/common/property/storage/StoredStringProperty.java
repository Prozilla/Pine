package dev.prozilla.pine.common.property.storage;

import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.core.storage.Storage;
import org.jetbrains.annotations.Contract;

public class StoredStringProperty extends StoredProperty<String> implements StringProperty {
	
	public StoredStringProperty(Storage storage, String key) {
		super(storage, key);
	}
	
	@Override
	public boolean setValue(String value) {
		return storage.setItem(key, value);
	}
	
	@Override
	public String getValue() {
		return storage.getItem(key);
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	public StringProperty toStringProperty() {
		return this;
	}
	
}
