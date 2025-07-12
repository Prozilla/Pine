package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.property.observable.ObservableProperty;
import dev.prozilla.pine.common.system.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deserializes data from a given JSON file.
 * @param <Data> The data type to deserialize the file to
 */
public class Deserializer<Data> extends ObservableProperty<Data> {
	
	protected final String path;
	private final Class<Data> dataType;
	private final ObjectMapper mapper;
	
	public Deserializer(String path, Class<Data> dataType) {
		this.path = path;
		this.dataType = dataType;
		
		mapper = new ObjectMapper();
		deserialize();
	}
	
	/**
	 * Deserializes the data from the file and stores it as the value of this property.
	 * @return The deserialized data.
	 */
	public Data deserialize() {
		try (InputStream inputStream = createInputStream()) {
			if (inputStream == null) {
				return null;
			}
			Data data = mapper.readValue(inputStream, dataType);
			setValue(data);
			return data;
		} catch (IOException e) {
			getLogger().error("Failed to deserialize: " + path, e);
		}
		
		return null;
	}
	
	/**
	 * Creates an input stream from the file.
	 * @return The input stream.
	 */
	protected InputStream createInputStream() {
		return ResourceUtils.getResourceStream(path);
	}
	
	/**
	 * Creates a property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 * @param <T> The type of value of the property
	 */
	public <T> DeserializedProperty<T> createProperty(DeserializedProperty.ValueFactory<Data, T> valueFactory) {
		return createProperty(valueFactory, null);
	}
	
	/**
	 * Creates a property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty or incomplete.
	 * @return The new property.
	 * @param <T> The type of value of the property
	 */
	public <T> DeserializedProperty<T> createProperty(DeserializedProperty.ValueFactory<Data, T> valueFactory, T fallbackValue) {
		return new DeserializedProperty<>(this, valueFactory, fallbackValue);
	}
	
}
