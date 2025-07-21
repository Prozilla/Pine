package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class FileDeserializerModule extends SimpleModule {
	
	/**
	 * Adds a deserializer to handle a specific type of value.
	 * @param deserializer The deserializer
	 * @param <T> The type of value
	 */
	public <T> SimpleModule addDeserializer(ValueDeserializer<T> deserializer) {
		return addDeserializer(deserializer.getType(), deserializer);
	}
	
}
