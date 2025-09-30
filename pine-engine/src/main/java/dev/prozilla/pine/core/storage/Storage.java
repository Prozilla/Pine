package dev.prozilla.pine.core.storage;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DimensionParser;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.dimension.DualDimensionParser;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.ColorParser;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.StorageConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Storage implements Initializable, Destructible {

	private final Map<String, String> entries;
	private final Application application;
	private final StorageConfig config;
	
	public Storage(Application application) {
		this.application = application;
		config = application.getConfig().storage;
		entries = new HashMap<>();
	}
	
	@Override
	public void init() {
		load();
	}
	
	@Override
	public void destroy() {
		save();
		clear();
	}
	
	public boolean hasItem(String key) {
		return entries.containsKey(key);
	}
	
	public DimensionBase getDimension(String key) {
		return getItem(key, new DimensionParser());
	}
	
	public DualDimension getDualDimension(String key) {
		return getItem(key, new DualDimensionParser());
	}
	
	public Color getColor(String key) {
		return getItem(key, new ColorParser());
	}
	
	public <T> T getItem(String key, Parser<T> parser) {
		Checks.isNotNull(parser, "parser");
		String value = getItem(key);
		if (value == null) {
			return null;
		}
		parser.parse(value);
		return parser.getResult();
	}
	
	public int getInt(String key) {
		String value = getItem(key);
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getItem(key));
	}
	
	public String getItem(String key) {
		if (config.loadOnRead.getValue()) {
			load();
		}
		return entries.get(key);
	}
	
	public void setItem(String key, Object value) {
		setItem(key, StringUtils.toString(value));
	}
	
	public void setItem(String key, String value) {
		entries.put(key, value);
		if (config.saveOnWrite.getValue()) {
			save();
		}
	}
	
	public void clear() {
		entries.clear();
	}
	
	public Set<Map.Entry<String, String>> getEntries() {
		return entries.entrySet();
	}
	
	public void load() {
		if (!config.enableLocalStorage.getValue()) {
			return;
		}
	
	}
	
	public void save() {
		if (!config.enableLocalStorage.getValue()) {
			return;
		}
		
	}

}
