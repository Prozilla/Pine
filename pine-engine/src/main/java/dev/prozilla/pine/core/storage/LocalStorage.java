package dev.prozilla.pine.core.storage;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * A key-value store that is saved as a properties file on the user's system.
 */
public class LocalStorage extends Storage {
	
	public static final String FILE_NAME = "local.properties";
	
	public LocalStorage(Application application) {
		super(application);
	}
	
	@Override
	public void load() {
		if (!config.enableLocalStorage.get()) {
			return;
		}
		
		String path = getPath();
		Properties properties = new Properties();
		
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		
		getLogger().logPath("Loading local storage", path);
		
		try (InputStream in = new FileInputStream(file)) {
			properties.load(in);
			setItems(properties);
		} catch (IOException e) {
			getLogger().error("Failed to load local storage from " + Logger.formatPath(path), e);
		}
		
		isInitialized = true;
	}
	
	public void setItems(Properties properties) {
		if (properties == null) {
			return;
		}
		boolean changed = false;
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			if (!Objects.equals(items.get(key), value)) {
				changed = true;
			}
			items.put(key, value);
		}
		if (changed && shouldSave()) {
			save();
		}
	}
	
	@Override
	public void save() {
		if (!config.enableLocalStorage.get() || !isInitialized) {
			return;
		}
		
		String path = getPath();
		File file = new File(path);
		
		if (items.isEmpty()) {
			// Delete the file if the store is empty
			if (file.exists()) {
				getLogger().logPath("Deleting local storage", path);
				file.delete();
			}
			return;
		}
		
		getLogger().logPath("Saving local storage", path);
		
		Properties properties = new Properties();
		for (Map.Entry<String, String> entry : items.entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		
		try (OutputStream out = new FileOutputStream(file)) {
			properties.store(out, null);
		} catch (IOException e) {
			getLogger().error("Failed to save local storage to " + Logger.formatPath(path), e);
		}
	}
	
	public String getPath() {
		return application.getPersistentDataPath() + FILE_NAME;
	}
	
}
