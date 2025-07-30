package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.core.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher extends EventDispatcher<DirectoryWatcher.EventType, String, Event<DirectoryWatcher.EventType, String>> {
	
	private final String path;
	private Thread watchThread;
	private final Path directory;
	private WatchService watchService;
	private volatile boolean watching = true;
	
	private static final Map<WatchEvent.Kind<?>, EventType> DIRECTORY_EVENT_TYPE_MAP = Map.of(
		ENTRY_CREATE, EventType.CREATED,
		ENTRY_DELETE, EventType.DELETED,
		ENTRY_MODIFY, EventType.MODIFIED
	);
	
	public enum EventType {
		CREATED,
		DELETED,
		MODIFIED
	}
	
	public DirectoryWatcher(String path) {
		this.path = PathUtils.onlyTrailingSlash(path);
		directory = getDirectoryPath(path);
		
		try {
			this.watchService = FileSystems.getDefault().newWatchService();
			directory.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			
			this.watchThread = new Thread(this::start);
			this.watchThread.setDaemon(true);
			this.watchThread.start();
		} catch (IOException e) {
			getLogger().error("Failed to initialize WatchService for " + path, e);
		}
	}
	
	@Override
	protected Event<EventType, String> createEvent(EventType eventType, String target) {
		return new Event<>(eventType, target);
	}
	
	/**
	 * Starts watching the directory for events.
	 */
	@SuppressWarnings("unchecked")
	private void start() {
		while (watching) {
			try {
				WatchKey key = watchService.take();
				Set<Event<EventType, String>> events = new HashSet<>();
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path target = ((WatchEvent<Path>)event).context();
					
					if (DIRECTORY_EVENT_TYPE_MAP.containsKey(kind)) {
						events.add(createEvent(DIRECTORY_EVENT_TYPE_MAP.get(kind), path + target.toString()));
					}
				}
				
				key.reset();
				for (Event<EventType, String> event : events) {
					invoke(event);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} catch (Exception e) {
				getLogger().error("Error while watching directory: " + directory, e);
			}
		}
	}
	
	/**
	 * Adds a listener that listens to changes to a given file.
	 * @param path Path to the file
	 * @param listener File change listener
	 */
	public EventListener<Event<EventType, String>> onFileChange(String path, EventListener<Event<EventType, String>> listener) {
		String normalizedPath = PathUtils.removeLeadingSlash(path);
		return addListener(EventType.MODIFIED, (event) -> {
			if (event.getTarget().equals(normalizedPath)) {
				listener.handle(event);
			}
		});
	}
	
	/**
	 * Stops the watch thread and closes the watch service.
	 */
	@Override
	public void destroy() {
		super.destroy();
		
		watching = false;
		if (watchThread != null) {
			watchThread.interrupt();
		}
		if (watchService != null) {
			try {
				watchService.close();
			} catch (IOException e) {
				getLogger().error("Failed to close WatchService", e);
			}
		}
	}
	
	private static Path getDirectoryPath(String path) {
		if (Application.isDevMode()) {
			File originalFile = new File("src/main/resources/" + PathUtils.removeLeadingSlash(path));
			if (originalFile.exists()) {
				return Path.of(originalFile.getAbsolutePath());
			}
		}
		
		return Paths.get(ResourceUtils.getResourcePath(path)).toAbsolutePath();
	}
	
}
