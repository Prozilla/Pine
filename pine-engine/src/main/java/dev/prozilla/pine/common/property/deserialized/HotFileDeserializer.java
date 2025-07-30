package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.system.DirectoryWatcher;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.core.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Deserializes data from a JSON file and hot reloads it whenever changes are detected.
 */
public class HotFileDeserializer<Data> extends FileDeserializer<Data> {
	
	private final Path filePath;
	private final DirectoryWatcher directoryWatcher;
	private final EventListener<Event<DirectoryWatcher.EventType, String>> listener;
	
	public HotFileDeserializer(DirectoryWatcher directoryWatcher, String path, Class<Data> dataType) {
		super(path, dataType);
		filePath = getFilePath(path);
		this.directoryWatcher = directoryWatcher;
		
		deserialize();
		listener = directoryWatcher.onFileChange(path, this::onFileChange);
	}
	
	/**
	 * Deserializes the file and updates the property of this value whenever the file changes.
	 * @param event The file change event
	 */
	protected void onFileChange(Event<DirectoryWatcher.EventType, String> event) {
		getLogger().log("File change detected: " + event.getTarget());
		deserialize();
	}
	
	@Override
	protected InputStream createInputStream() {
		if (filePath == null) {
			return null;
		}
		
		try {
			return new FileInputStream(filePath.toFile());
		} catch (FileNotFoundException e) {
			getLogger().error("File not found: " + path, e);
		}
		
		return null;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		directoryWatcher.removeListener(DirectoryWatcher.EventType.MODIFIED, listener);
	}
	
	private static Path getFilePath(String path) {
		if (Application.isDevMode()) {
			File originalFile = new File("src/main/resources/" + PathUtils.removeLeadingSlash(path));
			if (originalFile.exists()) {
				return Path.of(originalFile.getAbsolutePath());
			}
		}
		
		return Paths.get(ResourceUtils.getResourcePath(path)).toAbsolutePath();
	}

}
