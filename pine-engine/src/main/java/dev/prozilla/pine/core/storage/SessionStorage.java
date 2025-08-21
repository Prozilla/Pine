package dev.prozilla.pine.core.storage;

import dev.prozilla.pine.core.Application;

/**
 * A key-value store that is reset when the application closes.
 */
public class SessionStorage extends Storage {
	
	public SessionStorage(Application application) {
		super(application);
	}
	
	@Override
	protected void load() {
	
	}
	
	@Override
	protected void save() {
	
	}
	
}
