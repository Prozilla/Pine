package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.property.observable.SimpleObservableIntProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property whose value is retrieved from a deserialized file.
 */
public class DeserializedIntProperty extends SimpleObservableIntProperty {
	
	/**
	 * Factory method used to retrieve a value from a deserialized file.
	 * @param <Data> The deserialized file.
	 */
	@FunctionalInterface
	public interface ValueFactory<Data> {
		
		/**
		 * Retrieves a value from a deserialized file.
		 * @param data The deserialized file
		 * @return The value
		 */
		int create(Data data);
		
	}
	
	public <Data> DeserializedIntProperty(FileDeserializer<Data> source, ValueFactory<Data> valueFactory) {
		this(source, valueFactory, 0);
	}
	
	public <Data> DeserializedIntProperty(FileDeserializer<Data> source, ValueFactory<Data> valueFactory, int fallbackValue) {
		super(createValue(source.getValue(), valueFactory, fallbackValue));
		source.addObserver((data) -> set(createValue(data, valueFactory, fallbackValue)));
	}
	
	/**
	 * Retrieves the value from a given source of data using a value factory.
	 * @param data The data
	 * @param valueFactory The value factory
	 * @param fallbackValue An optional fallback value, for when the data is {@code null}
	 * @return The value retrieved from the data
	 * @param <Data> The type of data
	 */
	@Contract("null, _, _ -> param3")
	protected static <Data> int createValue(Data data, ValueFactory<Data> valueFactory, int fallbackValue) {
		if (data == null) {
			return fallbackValue;
		}
		
		return valueFactory.create(data);
	}
	
}
