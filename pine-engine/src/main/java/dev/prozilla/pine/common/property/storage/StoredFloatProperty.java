package dev.prozilla.pine.common.property.storage;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;
import dev.prozilla.pine.common.util.parser.ParseFunction;
import dev.prozilla.pine.core.storage.Storage;

public class StoredFloatProperty extends StoredProperty<Float> implements MutableFloatProperty {
	
	public StoredFloatProperty(Storage storage, String key) {
		super(storage, key);
	}
	
	@Override
	public boolean set(float value) {
		return setValue(Float.toString(value));
	}
	
	@Override
	public Float getValue() {
		return storage.getItem(key, ParseFunction::parseFloat);
	}
	
	@Override
	public float get() {
		return storage.getFloat(key);
	}
	
}
