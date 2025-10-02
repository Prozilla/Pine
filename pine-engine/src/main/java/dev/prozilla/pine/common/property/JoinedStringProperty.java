package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.CollectionProvider;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class JoinedStringProperty implements StringProperty, CollectionProvider<Property<String>> {
	
	private final String separator;
	private final List<Property<String>> stringProperties;
	
	@SafeVarargs
	public JoinedStringProperty(Property<String>... stringProperties) {
		this(null, stringProperties);
	}
	
	public JoinedStringProperty(String separator) {
		this(separator, (Property<String>[])null);
	}
	
	public JoinedStringProperty(String separator, Property<String>... stringProperties) {
		this.separator = separator;
		this.stringProperties = new ArrayList<>();
		addAll(stringProperties);
	}
	
	@Override
	public JoinedStringProperty append(Property<String> item) {
		add(item);
		return this;
	}
	
	@Override
	public JoinedStringProperty prepend(Property<String> item) {
		Checks.isNotNull(item, "item");
		stringProperties.addFirst(item);
		return this;
	}
	
	@Override
	public boolean add(Property<String> item) {
		Checks.isNotNull(item, "item");
		return stringProperties.add(item);
	}
	
	@Override
	public List<Property<String>> items() {
		return stringProperties;
	}
	
	@Override
	public String getValue() {
		StringJoiner stringJoiner = new StringJoiner(Objects.requireNonNullElse(separator, ""));
		for (Property<String> stringProperty : stringProperties) {
			stringJoiner.add(stringProperty.getValue());
		}
		return stringJoiner.toString();
	}
	
}
