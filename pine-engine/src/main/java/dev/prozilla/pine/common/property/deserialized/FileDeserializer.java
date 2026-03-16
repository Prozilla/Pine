package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.DualDimensionParser;
import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.property.bindable.SimpleBindableObjectProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.system.ResourceUtils;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Deserializes data from a given JSON file.
 *
 * This deserializer is represented by a property whose value is determined by deserializing the source file.
 * @param <Data> The data type to deserialize the file to
 */
public class FileDeserializer<Data> extends SimpleBindableObjectProperty<Data> {
	
	protected final String path;
	private final Class<Data> dataType;
	private static final ObjectMapper mapper = new ObjectMapper();
	protected boolean alwaysCreateData;
	
	public static final boolean ALWAYS_CREATE_DATA_DEFAULT = false;
	
	static {
		addDeserializers(
			new ValueDeserializer<>(Color.class, new ColorParser()),
			new ValueDeserializer<>(DimensionBase.class, new DimensionParser()),
			new ValueDeserializer<>(DualDimension.class, new DualDimensionParser()),
			new ValueDeserializer<>(Vector2f.class, new Vector2f.Parser()),
			new ValueDeserializer<>(Vector3f.class, new Vector3f.Parser()),
			new ValueDeserializer<>(Vector4f.class, new Vector4f.Parser()),
			new ValueDeserializer<>(Vector2i.class, new Vector2i.Parser()),
			new ValueDeserializer<>(Vector3i.class, new Vector3i.Parser()),
			new ValueDeserializer<>(Vector4i.class, new Vector4i.Parser())
		);
		resetFeatureStates();
	}
	
	/**
	 * Creates a file deserializer with {@link #alwaysCreateData} set to {@link #ALWAYS_CREATE_DATA_DEFAULT}.
	 * @param path The path of the source file
	 * @param dataType The data class
	 */
	public FileDeserializer(String path, Class<Data> dataType) {
		this(path, dataType, ALWAYS_CREATE_DATA_DEFAULT);
	}
	
	/**
	 * Creates a file deserializer.
	 * @param path The path of the source file
	 * @param dataType The data class
	 * @param alwaysCreateData Enables/disables the automatic creation of fallback data in case the source is missing or invalid.
	 */
	public FileDeserializer(String path, Class<Data> dataType, boolean alwaysCreateData) {
		this.path = path;
		this.dataType = dataType;
		this.alwaysCreateData = alwaysCreateData;
		
		deserialize();
	}
	
	/**
	 * Enables/disables the automatic creation of fallback data in case the source is missing or invalid.
	 *
	 * <p>When {@code alwaysCreateData} is {@code true}, the data object will (almost) never be null, and fields will be set to their default values if the source is missing or invalid.
	 * This can be useful when working with primitive values, because it allows for default values to be declared in a single place.</p>
	 * @param alwaysCreateData Whether to always create a data object
	 * @see #createFallbackData()
	 */
	public void setAlwaysCreateData(boolean alwaysCreateData) {
		if (this.alwaysCreateData == alwaysCreateData) {
			return;
		}
		
		this.alwaysCreateData = alwaysCreateData;
		
		if (alwaysCreateData && isNull()) {
			setValue(createFallbackData());
		} else if (!alwaysCreateData && isNotNull()) {
			deserialize();
		}
	}
	
	/**
	 * Deserializes the data from the file and stores it as the value of this property.
	 * @return The deserialized data.
	 */
	public Data deserialize() {
		Data data;
		try (InputStream inputStream = createInputStream()) {
			if (inputStream == null) {
				data = createFallbackData();
			} else {
				data = mapper.readValue(inputStream, dataType);
			}
		} catch (IOException e) {
			getLogger().error("Failed to deserialize: " + path, e);
			data = createFallbackData();
		}
		
		setValue(data);
		return data;
	}
	
	/**
	 * Creates a fallback data object when the deserialization has failed.
	 *
	 * <p>If {@link #alwaysCreateData} is {@code false}, {@code null} will be returned.</p>
	 *
	 * <p>If {@link #alwaysCreateData} is {@code true}, an empty data object will be created using the constructor of the data class without any arguments. If this fails, {@code null} is returned instead.</p>
	 * @return The fallback data object, or {@code null}.
	 */
	protected Data createFallbackData() {
		if (!alwaysCreateData) {
			return null;
		}
		
		try {
			return dataType.getConstructor().newInstance();
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	/**
	 * Creates an input stream from the file.
	 * @return The input stream.
	 */
	protected InputStream createInputStream() {
		try {
			return ResourceUtils.getResourceStream(path);
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * Creates a property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 * @param <T> The type of value of the property
	 */
	@Contract("_ -> new")
	public <T> DeserializedObjectProperty<T> createProperty(DeserializedObjectProperty.ValueFactory<Data, T> valueFactory) {
		return createProperty(valueFactory, null);
	}
	
	/**
	 * Creates a property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty or incomplete.
	 * @return The new property.
	 * @param <T> The type of value of the property
	 */
	@Contract("_, _ -> new")
	public <T> DeserializedObjectProperty<T> createProperty(DeserializedObjectProperty.ValueFactory<Data, T> valueFactory, T fallbackValue) {
		return new DeserializedObjectProperty<>(this, valueFactory, fallbackValue);
	}
	
	/**
	 * Creates an int property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 */
	@Contract("_ -> new")
	public DeserializedIntProperty createIntProperty(DeserializedIntProperty.ValueFactory<Data> valueFactory) {
		return createIntProperty(valueFactory, 0);
	}
	
	/**
	 * Creates an int property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty.
	 * @return The new property.
	 */
	@Contract("_, _ -> new")
	public DeserializedIntProperty createIntProperty(DeserializedIntProperty.ValueFactory<Data> valueFactory, int fallbackValue) {
		return new DeserializedIntProperty(this, valueFactory, fallbackValue);
	}
	
	/**
	 * Creates a float property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 */
	@Contract("_ -> new")
	public DeserializedFloatProperty createFloatProperty(DeserializedFloatProperty.ValueFactory<Data> valueFactory) {
		return createFloatProperty(valueFactory, 0f);
	}
	
	/**
	 * Creates a float property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty.
	 * @return The new property.
	 */
	@Contract("_, _ -> new")
	public DeserializedFloatProperty createFloatProperty(DeserializedFloatProperty.ValueFactory<Data> valueFactory, float fallbackValue) {
		return new DeserializedFloatProperty(this, valueFactory, fallbackValue);
	}
	
	/**
	 * Creates a boolean property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 */
	@Contract("_ -> new")
	public DeserializedBooleanProperty createBooleanProperty(DeserializedBooleanProperty.ValueFactory<Data> valueFactory) {
		return createBooleanProperty(valueFactory, false);
	}
	
	/**
	 * Creates a boolean property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty.
	 * @return The new property.
	 */
	@Contract("_, _ -> new")
	public DeserializedBooleanProperty createBooleanProperty(DeserializedBooleanProperty.ValueFactory<Data> valueFactory, boolean fallbackValue) {
		return new DeserializedBooleanProperty(this, valueFactory, fallbackValue);
	}
	
	/**
	 * Creates a string property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @return The new property.
	 */
	@Contract("_ -> new")
	public DeserializedStringProperty createStringProperty(DeserializedObjectProperty.ValueFactory<Data, String> valueFactory) {
		return createStringProperty(valueFactory, null);
	}
	
	/**
	 * Creates a string property whose value is retrieved from the deserialized data.
	 * @param valueFactory The method used to retrieve the value from the deserialized file.
	 * @param fallbackValue Fallback value to use in case the deserialized data is empty or incomplete.
	 * @return The new property.
	 */
	@Contract("_, _ -> new")
	public DeserializedStringProperty createStringProperty(DeserializedObjectProperty.ValueFactory<Data, String> valueFactory, String fallbackValue) {
		return new DeserializedStringProperty(this, valueFactory, fallbackValue);
	}
	
	/**
	 * Adds value deserializers to the object mapper using a {@link FileDeserializerModule}.
	 * @param deserializers The deserializers to add
	 */
	public static void addDeserializers(ValueDeserializer<?>... deserializers) {
		FileDeserializerModule module = new FileDeserializerModule();
		for (ValueDeserializer<?> deserializer : deserializers) {
			module.addDeserializer(deserializer);
		}
		mapper.registerModule(module);
	}
	
	/**
	 * Resets the states of features of the object mapper to their default state.
	 */
	public static void resetFeatureStates() {
		setFailOnNullForPrimitives(false);
		setFailOnUnknowProperties(false);
		setFailOnInvalidSubtype(false);
	}
	
	/**
	 * Sets the state of {@link DeserializationFeature#FAIL_ON_NULL_FOR_PRIMITIVES} for the object mapper.
	 * @param state Whether to enable or disable the feature
	 */
	public static void setFailOnNullForPrimitives(boolean state) {
		setFeatureState(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, state);
	}
	
	/**
	 * Sets the state of {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} for the object mapper.
	 * @param state Whether to enable or disable the feature
	 */
	public static void setFailOnUnknowProperties(boolean state) {
		setFeatureState(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, state);
	}
	
	/**
	 * Sets the state of {@link DeserializationFeature#FAIL_ON_INVALID_SUBTYPE} for the object mapper.
	 * @param state Whether to enable or disable the feature
	 */
	public static void setFailOnInvalidSubtype(boolean state) {
		setFeatureState(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, state);
	}
	
	/**
	 * Enables or disables a deserialization feature of the object mapper.
	 * @param feature The feature to enable or disable
	 * @param state Whether to enable or disable the feature
	 */
	public static void setFeatureState(DeserializationFeature feature, boolean state) {
		mapper.configure(feature, state);
	}
	
}
