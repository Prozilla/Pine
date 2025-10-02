package dev.prozilla.pine.common.property.storage;

import dev.prozilla.pine.common.property.mutable.MutableIntProperty;
import dev.prozilla.pine.core.storage.Storage;

public class StoredIntProperty extends StoredProperty<Integer> implements MutableIntProperty {
	
	public StoredIntProperty(Storage storage, String key) {
		super(storage, key);
	}
	
	@Override
	public boolean set(int value) {
		return setValue(Integer.toString(value));
	}
	
	@Override
	public int get() {
		return storage.getInt(key);
	}
	
}
