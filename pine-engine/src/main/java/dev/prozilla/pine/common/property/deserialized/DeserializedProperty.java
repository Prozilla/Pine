package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.property.observable.ObservableProperty;

/**
 * Represents a property whose value is retrieved from a deserialized file.
 */
public class DeserializedProperty<T> extends ObservableProperty<T> {
	
	/**
	 * Factory method used to retrieve a value from a deserialized file.
	 * @param <Data> The deserialized file.
	 * @param <Value> The type of value to retrieve.
	 */
	@FunctionalInterface
	public interface ValueFactory<Data, Value> {
		
		/**
		 * Retrieves a value from a deserialized file.
		 * @param data The deserialized file
		 * @return The value
		 */
		Value create(Data data);
		
	}
	
	public <Data> DeserializedProperty(Deserializer<Data> source, ValueFactory<Data, T> valueFactory) {
		this(source, valueFactory, null);
	}
	
	public <Data> DeserializedProperty(Deserializer<Data> source, ValueFactory<Data, T> valueFactory, T fallbackValue) {
		super(createValue(source.getValue(), valueFactory, fallbackValue));
		source.addObserver((data) -> setValue(createValue(data, valueFactory, fallbackValue)));
	}
	
	protected static <Data, V> V createValue(Data data, ValueFactory<Data, V> valueFactory, V fallbackValue) {
		if (data == null) {
			return fallbackValue;
		}
		
		V value = valueFactory.create(data);
		if (value == null) {
			return fallbackValue;
		}
		
		return value;
	}
	
}
