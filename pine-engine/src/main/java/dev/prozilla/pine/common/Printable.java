package dev.prozilla.pine.common;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract interface for printable objects.
 */
public interface Printable {
	
	String FIELDS_PREFIX = "[";
	String FIELDS_SUFFIX = "]";
	String FIELDS_SEPARATOR = ", ";
	String FIELD_VALUE_SEPARATOR = "=";
	
	/**
	 * Returns a string representation of this object.
	 * @return String representation of this object.
	 */
	@NotNull String toString();
	
	/**
	 * Prints this object using the system logger then terminates the line.
	 */
	default void print() {
		print(Logger.system);
	}
	
	/**
	 * Prints this object then terminates the line.
	 */
	default void print(Logger logger) {
		logger.log(this);
	}
	
	/**
	 * Converts an object and its fields into a string representation.
	 * @param object The object
	 * @param fields The fields of the object, each pair representing the name and value of a field
	 * @return The string representation of the object.
	 */
	static String objectToString(Object object, Object... fields) {
		if (object == null) {
			return "null";
		}
		Checks.array(fields, "fields").hasLength().isEven();
		if (fields.length == 0) {
			return object.getClass().getSimpleName();
		}
		
		StringBuilder stringBuilder = new StringBuilder(object.getClass().getSimpleName()).append(FIELDS_PREFIX);
		for (int i = 0; i < fields.length; i += 2) {
			if (i > 0) {
				stringBuilder.append(FIELDS_SEPARATOR);
			}
			stringBuilder.append(fields[i]).append(FIELD_VALUE_SEPARATOR).append(fields[i + 1]);
		}
		stringBuilder.append(FIELDS_SUFFIX);
		return stringBuilder.toString();
	}
	
}
