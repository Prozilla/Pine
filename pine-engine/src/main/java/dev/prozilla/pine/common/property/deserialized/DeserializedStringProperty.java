package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.property.StringProperty;

public class DeserializedStringProperty extends DeserializedObjectProperty<String> implements StringProperty {
	
	public <Data> DeserializedStringProperty(FileDeserializer<Data> source, ValueFactory<Data, String> valueFactory) {
		this(source, valueFactory, null);
	}
	
	public <Data> DeserializedStringProperty(FileDeserializer<Data> source, ValueFactory<Data, String> valueFactory, String fallbackValue) {
		super(source, valueFactory, fallbackValue);
	}
	
}
