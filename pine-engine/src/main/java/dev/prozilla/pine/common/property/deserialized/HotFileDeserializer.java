package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.FileWatcher;
import dev.prozilla.pine.common.system.ResourceUtils;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Deserializes data from a JSON file and hot reloads it whenever changes are detected.
 */
public class HotFileDeserializer<Data> extends FileDeserializer<Data> implements FileWatcher {
	
	private final Path filePath;
	private final DirectoryWatcher directoryWatcher;
	private final EventListener<Event<DirectoryWatcher.EventType, String>> listener;
	
	public HotFileDeserializer(DirectoryWatcher directoryWatcher, String path, Class<Data> dataType) {
		this(directoryWatcher, path, dataType, ALWAYS_CREATE_DATA_DEFAULT);
	}
	
	public HotFileDeserializer(DirectoryWatcher directoryWatcher, String path, Class<Data> dataType, boolean alwaysCreateData) {
		super(path, dataType, alwaysCreateData);
		filePath = ResourceUtils.getResourceFilePath(path);
		this.directoryWatcher = directoryWatcher;
		
		deserialize();
		listener = directoryWatcher.watch(this);
	}
	
	/**
	 * Deserializes the file and updates the property of this value whenever the file changes.
	 * @param event The file change event
	 */
	@Override
	public void onFileChange(Event<DirectoryWatcher.EventType, String> event) {
		deserialize();
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	protected InputStream createInputStream() {
		return ResourceUtils.createResourceFileInputStream(path, filePath);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		directoryWatcher.removeFileChangeListener(listener);
	}

}
