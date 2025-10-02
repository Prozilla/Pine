package dev.prozilla.pine.common.property.storage;

import dev.prozilla.pine.common.property.mutable.MutableBooleanProperty;
import dev.prozilla.pine.core.storage.Storage;

public class StoredBooleanProperty extends StoredProperty<Boolean> implements MutableBooleanProperty {
	
	public StoredBooleanProperty(Storage storage, String key) {
		super(storage, key);
	}
	
	@Override
	public boolean set(boolean value) {
		return setValue(Boolean.toString(value));
	}
	
	@Override
	public boolean get() {
		return storage.getBoolean(key);
	}
	
}
