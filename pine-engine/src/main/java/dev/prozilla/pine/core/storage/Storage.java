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
import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.util.BooleanUtils;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.parser.ParseFunction;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.StorageConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A key-value store that uses strings for keys and values.
 */
public abstract class Storage implements Initializable, Destructible, Transceivable<Storage>, Printable {

	/** The items in this store. */
	protected final Map<String, String> items;
	protected final Application application;
	protected final StorageConfig config;
	protected boolean isInitialized;
	
	/**
	 * Creates a new key-value store.
	 */
	public Storage(Application application) {
		this.application = application;
		config = application.getConfig().storage;
		items = new HashMap<>();
		isInitialized = false;
	}
	
	/**
	 * Loads items into this store.
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
	 * Saves the items from this store and clears the memory.
	 */
	@Override
	public void destroy() {
		if (isInitialized) {
			save();
		}
		
		// We do this to make sure we don't save after removing all items,
		// Since saving is not allowed if the store has not been initialized yet
		// This also has the added benefit of forcing the store to reload if it's used after being destroyed
		isInitialized = false;
		
		clear();
	}
	
	/**
	 * Overwrites the items in this store with the items from another store.
	 * @param source The store to read the items from
	 */
	public void overwrite(Storage source) {
		items.clear();
		receive(source);
	}
	
	/**
	 * Copies the items of this store to another store.
	 * @param target The store to copy the items to
	 */
	@Override
	public void transmit(Storage target) {
		Checks.isNotNull(target, "target");
		if (shouldLoad()) {
			load();
		}
		target.setItems(items.entrySet());
	}
	
	@Override
	public Storage self() {
		return this;
	}
	
	/**
	 * Checks if this store has an item with a given key.
	 * @param key The key of the item
	 * @return {@code true} if an item exists in this store with the given key.
	 */
	public boolean hasItem(String key) {
		if (shouldLoad()) {
			load();
		}
		return items.containsKey(key);
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector2f.Parser
	 */
	public Vector2f getVector2f(String key) {
		return getItem(key, new Vector2f.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector2i.Parser
	 */
	public Vector2i getVector2i(String key) {
		return getItem(key, new Vector2i.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector3f.Parser
	 */
	public Vector3f getVector3f(String key) {
		return getItem(key, new Vector3f.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector3i.Parser
	 */
	public Vector3i getVector3i(String key) {
		return getItem(key, new Vector3i.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector4f.Parser
	 */
	public Vector4f getVector4f(String key) {
		return getItem(key, new Vector4f.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see Vector4i.Parser
	 */
	public Vector4i getVector4i(String key) {
		return getItem(key, new Vector4i.Parser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see DimensionParser
	 */
	public DimensionBase getDimension(String key) {
		return getItem(key, new DimensionParser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see DualDimensionParser
	 */
	public DualDimension getDualDimension(String key) {
		return getItem(key, new DualDimensionParser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 * @see ColorParser
	 */
	public Color getColor(String key) {
		return getItem(key, new ColorParser());
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @param parser The parser to use
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
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
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @param parseFunction The parsing function to use
	 * @return The parsed value of the item, or {@code null} if the item does not exist or the parsing failed.
	 */
	public <T> T getItem(String key, ParseFunction<T> parseFunction) {
		Checks.isNotNull(parseFunction, "parseFunction");
		String value = getItem(key);
		if (value == null) {
			return null;
		}
		return parseFunction.parse(value);
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code 0} if the item does not exist.
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
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code 0} if the item does not exist.
	 * @see Float#parseFloat(String)
	 * @throws NumberFormatException If the parsing failed.
	 */
	public float getFloat(String key) throws NumberFormatException {
		String value = getItem(key);
		if (value == null) {
			return 0;
		}
		return Float.parseFloat(value);
	}
	
	/**
	 * Returns the value of the item in this store with a given key by parsing it.
	 * @param key The key of the item
	 * @return The parsed value of the item, or {@code false} if the item does not exist.
	 * @see Boolean#parseBoolean(String) 
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getItem(key));
	}
	
	/**
	 * Returns the value of the item in this store with a given key.
	 * @param key The key of the item
	 * @return The value of the item, or {@code null} if the item does not exist.
	 */
	public String getItem(String key) {
		if (shouldLoad()) {
			load();
		}
		return items.get(key);
	}
	
	/**
	 * Sets the values of multiple items.
	 * @param items The key and value pairs
	 * @return {@code true} if an item was changed.
	 */
	public <O> boolean setItems(Map<String, O> items) {
		return setItems(items.entrySet());
	}
	
	/**
	 * Sets the values of multiple items.
	 * @param items The key and value pairs
	 * @return {@code true} if an item was changed.
	 */
	protected <O> boolean setItems(Set<Map.Entry<String, O>> items) {
		if (items == null) {
			return false;
		}
		boolean changed = false;
		for (Map.Entry<String, O> item : items) {
			String key = item.getKey();
			String value = StringUtils.toString(item.getValue());
			if (!Objects.equals(this.items.get(key), value)) {
				changed = true;
			}
			this.items.put(key, value);
		}
		if (changed && shouldSave()) {
			save();
		}
		return changed;
	}
	
	/**
	 * Sets the value of the item with a given key to the string representation of an object.
	 * @param key The key of the item
	 * @param value The new value
	 * @return {@code true} if the item was changed.
	 * @see StringUtils#toString(Object)
	 */
	public boolean setItem(String key, Object value) {
		return setItem(key, StringUtils.toString(value));
	}
	
	/**
	 * Sets the value of the item with a given key.
	 * @param key The key of the item
	 * @param value The new value
	 * @return {@code true} if the item was changed.
	 */
	public boolean setItem(String key, String value) {
		if (Objects.equals(items.get(key), value)) {
			return false;
		}
		items.put(key, value);
		if (shouldSave()) {
			save();
		}
		return true;
	}
	
	/**
	 * Removes the item with a given key.
	 * @param key The key of the item
	 * @return {@code false} if there was no item with the given key.
	 */
	public boolean removeItem(String key) {
		if (items.remove(key) != null) {
			if (shouldSave()) {
				save();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Removes all items from this store.
	 */
	public void removeAll() {
		if (shouldLoad()) {
			load();
		}
		if (items.isEmpty()) {
			return;
		}
		clear();
	}
	
	/**
	 * Removes all items from this store that are currently in memory.
	 */
	public void clear() {
		items.clear();
		if (shouldSave()) {
			save();
		}
	}
	
	/**
	 * Returns the amount of items in this store.
	 * @return The amount of items in this store.
	 */
	public int size() {
		if (shouldLoad()) {
			load();
		}
		return items.size();
	}
	
	/**
	 * Returns the items in this store.
	 * @return The items in this store.
	 */
	public Set<Map.Entry<String, String>> items() {
		return items.entrySet();
	}
	
	/**
	 * Returns the keys of the items in this store.
	 * @return The keys in this store.
	 */
	public Set<String> keys() {
		return items.keySet();
	}
	
	/**
	 * Returns the values of the items in this store.
	 * @return The values in this store.
	 */
	public Collection<String> values() {
		return items.values();
	}
	
	protected boolean shouldLoad() {
		return !isInitialized || BooleanUtils.isTrue(config.loadOnRead.getValue());
	}
	
	protected boolean shouldSave() {
		return isInitialized && BooleanUtils.isTrue(config.saveOnWrite.getValue());
	}
	
	/**
	 * Loads items into this store.
	 */
	protected abstract void load();
	
	/**
	 * Saves the items from this store.
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
		return items.toString();
	}
	
}
