package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.DualDimensionParser;
import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.property.observable.SimpleObservableObjectProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.system.ResourceUtils;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Deserializes data from a given JSON file.
 * @param <Data> The data type to deserialize the file to
 */
public class FileDeserializer<Data> extends SimpleObservableObjectProperty<Data> {
	
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
	
	public FileDeserializer(String path, Class<Data> dataType) {
		this(path, dataType, ALWAYS_CREATE_DATA_DEFAULT);
	}
	
	public FileDeserializer(String path, Class<Data> dataType, boolean alwaysCreateData) {
		this.path = path;
		this.dataType = dataType;
		this.alwaysCreateData = alwaysCreateData;
		
		deserialize();
	}
	
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
		return ResourceUtils.getResourceStream(path);
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
