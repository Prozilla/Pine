package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.event.Event;

public class ConfigOptionEvent<T> extends Event<ConfigOptionEvent.Type, ConfigOption<T>> {
	
	private final T value;
	
	public enum Type {
		CHANGE,
		RESET
	}
	
	public ConfigOptionEvent(Type type, ConfigOption<T> target, T value) {
		super(type, target);
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}

}
