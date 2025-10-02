package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.property.observable.ObservableObjectProperty;
import org.jetbrains.annotations.Contract;

/**
 * Represents a property whose value is retrieved from a deserialized file.
 */
public class DeserializedProperty<T> extends ObservableObjectProperty<T> {
	
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
	
	public <Data> DeserializedProperty(FileDeserializer<Data> source, ValueFactory<Data, T> valueFactory) {
		this(source, valueFactory, null);
	}
	
	public <Data> DeserializedProperty(FileDeserializer<Data> source, ValueFactory<Data, T> valueFactory, T fallbackValue) {
		super(createValue(source.getValue(), valueFactory, fallbackValue));
		source.addObserver((data) -> setValue(createValue(data, valueFactory, fallbackValue)));
	}
	
	/**
	 * Retrieves the value from a given source of data using a value factory.
	 * @param data The data
	 * @param valueFactory The value factory
	 * @param fallbackValue An optional fallback value, for when the value factory returns {@code null}
	 * @return The value retrieved from the data
	 * @param <Data> The type of data
	 * @param <V> The type of value
	 */
	@Contract("null, _, _ -> param3")
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
