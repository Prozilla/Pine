package dev.prozilla.pine.core.storage;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.Transceivable;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.DualDimensionParser;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.StorageConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A key-value store that uses strings for keys and values.
 */
public abstract class Storage implements Initializable, Destructible, Transceivable<Storage>, Printable {

	/** The entries in this storage. */
	protected final Map<String, String> entries;
	protected final Application application;
	protected final StorageConfig config;
	protected boolean isInitialized;
	
	/**
	 * Creates a new key-value store.
	 */
	public Storage(Application application) {
		this.application = application;
		config = application.getConfig().storage;
		entries = new HashMap<>();
		isInitialized = false;
	}
	
	/**
	 * Loads entries into this store.
	 */
	@Override
	public void init() {
		if (isInitialized) {
			return;
		}
		load();
		
		isInitialized = true;
	}
	
	/**
	 * Saves the entries from this store and clears the memory.
	 */
	@Override
	public void destroy() {
		save();
		
		// We do this to make sure we don't save after removing all entries,
		// Since saving is not allowed if the store has not been initialized yet
		// This also has the added benefit of forcing the store to reload if it's used after being destroyed
		isInitialized = false;
		
		clear();
	}
	
	/**
	 * Overwrites the entries in this store with the entries from another store.
	 * @param source The store to read the entries from
	 */
	public void overwrite(Storage source) {
		entries.clear();
		receive(source);
	}
	
	/**
	 * Copies the entries of this store to another store.
	 * @param target The store to copy the entries to
	 */
	@Override
	public void transmit(Storage target) {
		Checks.isNotNull(target, "target");
		if (shouldLoad()) {
			load();
		}
		target.setItems(entries.entrySet());
	}
	
	@Override
	public Storage self() {
		return this;
	}
	
	/**
	 * Checks if this store has an entry with a given key.
	 * @param key The key of the entry
	 * @return {@code true} if an entry exists in this store with the given key.
	 */
	public boolean hasItem(String key) {
		if (shouldLoad()) {
			load();
		}
		return entries.containsKey(key);
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @return The parsed value of the entry, or {@code null} if the entry does not exist or the parsing failed.
	 * @see DimensionParser
	 */
	public DimensionBase getDimension(String key) {
		return getItem(key, new DimensionParser());
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @return The parsed value of the entry, or {@code null} if the entry does not exist or the parsing failed.
	 * @see DualDimensionParser
	 */
	public DualDimension getDualDimension(String key) {
		return getItem(key, new DualDimensionParser());
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @return The parsed value of the entry, or {@code null} if the entry does not exist or the parsing failed.
	 * @see ColorParser
	 */
	public Color getColor(String key) {
		return getItem(key, new ColorParser());
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @param parser The parser to use
	 * @return The parsed value of the entry, or {@code null} if the entry does not exist or the parsing failed.
	 */
	public <T> T getItem(String key, Parser<T> parser) {
		Checks.isNotNull(parser, "parser");
		String value = getItem(key);
		if (value == null) {
			return null;
		}
		parser.parse(value);
		return parser.getResult();
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @return The parsed value of the entry, or {@code 0} if the entry does not exist.
	 * @see Integer#parseInt(String)
	 * @throws NumberFormatException If the parsing failed.
	 */
	public int getInt(String key) throws NumberFormatException {
		String value = getItem(key);
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * Returns the value of an entry in this store with a given key by parsing it.
	 * @param key The key of the entry
	 * @return The parsed value of the entry, or {@code false} if the entry does not exist.
	 * @see Boolean#parseBoolean(String) 
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getItem(key));
	}
	
	/**
	 * Returns the value of an entry in this store with a given key.
	 * @param key The key of the entry
	 * @return The value of the entry, or {@code null} if the entry does not exist.
	 */
	public String getItem(String key) {
		if (shouldLoad()) {
			load();
		}
		return entries.get(key);
	}
	
	/**
	 * Sets the values of multiple entries.
	 * @param items The key and value pairs
	 */
	public <O> void setItems(Map<String, O> items) {
		setItems(items.entrySet());
	}
	
	/**
	 * Sets the values of multiple entries.
	 * @param items The key and value pairs
	 */
	protected <O> void setItems(Set<Map.Entry<String, O>> items) {
		if (items == null) {
			return;
		}
		boolean changed = false;
		for (Map.Entry<String, O> item : items) {
			String key = item.getKey();
			String value = StringUtils.toString(item.getValue());
			if (!Objects.equals(entries.get(key), value)) {
				changed = true;
			}
			entries.put(key, value);
		}
		if (changed && shouldSave()) {
			save();
		}
	}
	
	/**
	 * Sets the value of an entry with a given key to the string representation of an object.
	 * @param key The key of the entry
	 * @param value The new value
	 * @see StringUtils#toString(Object)
	 */
	public void setItem(String key, Object value) {
		setItem(key, StringUtils.toString(value));
	}
	
	/**
	 * Sets the value of an entry with a given key.
	 * @param key The key of the entry
	 * @param value The new value
	 */
	public void setItem(String key, String value) {
		if (Objects.equals(entries.get(key), value)) {
			return;
		}
		entries.put(key, value);
		if (shouldSave()) {
			save();
		}
	}
	
	/**
	 * Removes an entry with a given key.
	 * @param key The key of the entry
	 */
	public void removeItem(String key) {
		if (entries.remove(key) != null && shouldSave()) {
			save();
		}
	}
	
	/**
	 * Removes all entries from this store.
	 */
	public void clear() {
		entries.clear();
		if (shouldSave()) {
			save();
		}
	}
	
	/**
	 * Returns the amount of entries in this store.
	 * @return The amount of entries in this store.
	 */
	public int size() {
		if (shouldLoad()) {
			load();
		}
		return entries.size();
	}
	
	/**
	 * Returns the entries in this store.
	 * @return The entries in this store.
	 */
	public Set<Map.Entry<String, String>> entries() {
		return entries.entrySet();
	}
	
	/**
	 * Returns the keys of the entries in this store.
	 * @return The keys of the entries in this store.
	 */
	public Set<String> keys() {
		return entries.keySet();
	}
	
	/**
	 * Returns the values of the entries in this store.
	 * @return The values of the entries in this store.
	 */
	public Collection<String> values() {
		return entries.values();
	}
	
	protected boolean shouldLoad() {
		return !isInitialized || BooleanUtils.isTrue(config.loadOnRead.getValue());
	}
	
	protected boolean shouldSave() {
		return isInitialized && BooleanUtils.isTrue(config.saveOnWrite.getValue());
	}
	
	/**
	 * Loads entries into this store.
	 */
	protected abstract void load();
	
	/**
	 * Saves entries from this store.
	 */
	protected abstract void save();
	
	@Override
	public void print() {
		print(getLogger());
	}
	
	protected Logger getLogger() {
		return application.getLogger();
	}
	
	@Override
	public @NotNull String toString() {
		return entries.toString();
	}
	
}
