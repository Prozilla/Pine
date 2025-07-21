package dev.prozilla.pine.core.component.deserialization;

import dev.prozilla.pine.common.property.deserialized.FileDeserializer;
import dev.prozilla.pine.common.system.Directory;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Component;

/**
 * A component that consumes deserialized data from a JSON file.
 * @param <Data> The type of data this component consumes
 */
public class DeserializedData<Data> extends Component {
	
	protected FileDeserializer<Data> deserializer;
	
	/**
	 * Creates a file deserializer and reads data from it.
	 *
	 * <p>If the application is in developer mode, hot reloading will be enabled.</p>
	 * @param directory The directory of the file
	 * @param fileName The name of the file
	 * @param dataType The type of data to deserialize to
	 */
	public void readData(Directory directory, String fileName, Class<Data> dataType) {
		readData(directory, fileName, dataType, Application.isDevMode());
	}
	
	/**
	 * Creates a file deserializer and reads data from it.
	 * @param directory The directory of the file
	 * @param fileName The name of the file
	 * @param dataType The type of data to deserialize to
	 * @param hotReload {@code true} enables hot reloading
	 */
	public void readData(Directory directory, String fileName, Class<Data> dataType, boolean hotReload) {
		readData(directory.createFileDeserializer(fileName, dataType, hotReload));
	}
	
	/**
	 * Reads the data from a file deserializer.
	 *
	 * <p>This method can be used to initialize the component and create properties based on the data from the deserialized file.</p>
	 * @param deserializer The file deserializer
	 */
	public void readData(FileDeserializer<Data> deserializer) {
		this.deserializer = deserializer;
	}
	
	/**
	 * Returns the deserialized data.
	 * @return The deserialized data.
	 */
	public Data getData() {
		return deserializer.getValue();
	}
	
	/**
	 * Destroys this component and its file deserializer.
	 */
	@Override
	public void destroy() {
		super.destroy();
		deserializer.destroy();
	}
	
}
