package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.core.state.config.option.ObjectConfigOption;

public class ConfigOptionEvent<T> extends Event<ConfigOptionEvent.Type, ObjectConfigOption<T>> {
	
	private final T value;
	
	public enum Type {
		CHANGE,
		RESET
	}
	
	public ConfigOptionEvent(Type type, ObjectConfigOption<T> target, T value) {
		super(type, target);
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}

}
