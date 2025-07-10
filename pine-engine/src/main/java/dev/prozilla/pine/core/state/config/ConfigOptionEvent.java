package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.event.Event;

public class ConfigOptionEvent<T> extends Event<ConfigOptionEventType, ConfigOption<T>> {
	
	private final T value;
	
	public ConfigOptionEvent(ConfigOptionEventType type, ConfigOption<T> target, T value) {
		super(type, target);
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
}
