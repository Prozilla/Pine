package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidObjectException;

import java.util.function.Consumer;

/**
 * Utility class for performing checks on strings.
 */
public final class StringChecks extends ChecksBase<String, StringChecks> implements IterableChecks<StringChecks> {
	
	public StringChecks(String value) {
		this(value, null);
	}
	
	public StringChecks(String value, String name) {
		super(value, name);
	}
	
	@Override
	public StringChecks hasLength(Consumer<IntChecks> lengthChecks) throws InvalidObjectException {
		lengthChecks.accept(hasLength());
		return this;
	}
	
	@Override
	public IntChecks hasLength() {
		isNotNull();
		return new IntChecks(value.length(), "length of " + name);
	}
	
	public StringChecks isNotBlank() {
		isNotNull();
		Checks.isNotBlank(value, name + " must not be blank");
		return this;
	}
	
	@Override
	public StringChecks isNotEmpty() {
		isNotNull();
		Checks.isNotEmpty(value, name + " must not be empty");
		return this;
	}
	
	public StringChecks startsWith(String prefix) {
		isNotNull();
		Checks.hasPrefix(value, prefix, String.format("%s must start with '%s'", name, prefix));
		return this;
	}
	
	public StringChecks endsWith(String suffix) {
		isNotNull();
		Checks.hasSuffix(value, suffix, String.format("%s must end with '%s'", name, suffix));
		return this;
	}
	
	@Override
	protected StringChecks self() {
		return this;
	}
	
	@Override
	protected String getDefaultName() {
		return "string";
	}
}
