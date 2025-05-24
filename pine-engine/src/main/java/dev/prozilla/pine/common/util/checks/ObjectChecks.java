package dev.prozilla.pine.common.util.checks;

/**
 * Utility class for performing checks on objects.
 */
public final class ObjectChecks extends ChecksBase<Object, ObjectChecks> {
	
	public ObjectChecks(Object value, String name) {
		super(value, name);
	}
	
	@Override
	protected ObjectChecks self() {
		return this;
	}
	
	@Override
	protected String getDefaultName() {
		return "object";
	}
}
