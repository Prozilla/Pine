package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.property.deserialized.FileDeserializer;
import dev.prozilla.pine.common.property.deserialized.HotFileDeserializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a directory from the file system.
 *
 * <p>Can be used to watch a directory for file changes.</p>
 */
public class Directory implements Destructible, Cloneable<Directory>, Printable {
	
	private final String path;
	private DirectoryWatcher watcher;
	
	public Directory(String path) {
		this.path = PathUtils.onlyTrailingSlash(path);
	}
	
	/**
	 * Creates a deserializer for a given file.
	 * @param relativePath The path of the file relative to this directory
	 * @param dataType The data type to deserialize the file to
	 * @param hot {@code true} enables hot reloading
	 * @return The new file deserializer.
	 * @param <Data> The type to deserialize the file to
	 */
	public <Data> FileDeserializer<Data> createFileDeserializer(String relativePath, Class<Data> dataType, boolean hot) {
		if (hot) {
			return createHotFileDeserializer(relativePath, dataType);
		} else {
			return createFileDeserializer(relativePath, dataType);
		}
	}
	
	/**
	 * Creates a file deserializer.
	 * @param relativePath The path of the file relative to this directory
	 * @param dataType The data type to deserialize the file to
	 * @return The new file deserializer.
	 * @param <Data> The type to deserialize the file to
	 */
	public <Data> FileDeserializer<Data> createFileDeserializer(String relativePath, Class<Data> dataType) {
		return new FileDeserializer<>(resolvePath(relativePath), dataType);
	}
	
	/**
	 * Creates a file deserializer with hot reloading.
	 * @param relativePath The path of the file relative to this directory
	 * @param dataType The data type to deserialize the file to
	 * @return The new file deserializer with hot reloading.
	 * @param <Data> The type to deserialize the file to
	 */
	@Contract("_, _ -> new")
	public <Data> HotFileDeserializer<Data> createHotFileDeserializer(String relativePath, Class<Data> dataType) {
		return new HotFileDeserializer<>(getWatcher(), resolvePath(relativePath), dataType);
	}
	
	/**
	 * Creates a new watcher for this directory or returns the existing one if there already is one.
	 * @return The directory watcher.
	 */
	@Contract("-> !null")
	public DirectoryWatcher getWatcher() {
		if (watcher == null) {
			watcher = new DirectoryWatcher(path);
		}
		
		return watcher;
	}
	
	public String getPath() {
		return path;
	}
	
	/**
	 * Resolves a path relative to this directory.
	 * @param relativePath The path relative to this directory.
	 * @return The resolved path.
	 */
	public String resolvePath(String relativePath) {
		return path + PathUtils.removeLeadingSlash(relativePath);
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object || (object instanceof Directory directory && equals(directory));
	}
	
	@Override
	public boolean equals(Directory directory) {
		return directory != null && path.equals(directory.getPath());
	}
	
	@Override
	public Directory clone() {
		return new Directory(path);
	}
	
	@Override
	public @NotNull String toString() {
		return path;
	}
	
	/**
	 * Destroys the watcher of this directory.
	 */
	@Override
	public void destroy() {
		if (watcher != null) {
			watcher.destroy();
			watcher = null;
		}
	}
	
}
