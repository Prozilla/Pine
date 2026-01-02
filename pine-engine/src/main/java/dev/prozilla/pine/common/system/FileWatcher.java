package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.event.Event;

public interface FileWatcher {
	
	void onFileChange(Event<DirectoryWatcher.EventType, String> event);
	
	String getPath();
	
}
