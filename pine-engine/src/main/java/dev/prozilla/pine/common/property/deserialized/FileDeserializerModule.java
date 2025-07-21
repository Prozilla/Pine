package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class FileDeserializerModule extends SimpleModule {
	
	public <T> SimpleModule addDeserializer(ValueDeserializer<T> deserializer) {
		return addDeserializer(deserializer.getType(), deserializer);
	}
	
}
