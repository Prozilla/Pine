package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.CollectionProvider;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A decorator for string properties which joins strings.
 */
public class JoinedStringProperty implements StringProperty, CollectionProvider<StringProperty> {
	
	private final String separator;
	private final List<StringProperty> stringProperties;
	
	public JoinedStringProperty(StringProperty... stringProperties) {
		this(null, stringProperties);
	}
	
	public JoinedStringProperty(String separator) {
		this(separator, (StringProperty[])null);
	}
	
	public JoinedStringProperty(String separator, StringProperty... stringProperties) {
		this.separator = separator;
		this.stringProperties = new ArrayList<>();
		addAll(stringProperties);
	}
	
	@Override
	public JoinedStringProperty append(StringProperty item) {
		add(item);
		return this;
	}
	
	@Override
	public JoinedStringProperty prepend(StringProperty item) {
		Checks.isNotNull(item, "item");
		stringProperties.addFirst(item);
		return this;
	}
	
	@Override
	public boolean add(StringProperty item) {
		Checks.isNotNull(item, "item");
		return stringProperties.add(item);
	}
	
	@Override
	public List<StringProperty> items() {
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
