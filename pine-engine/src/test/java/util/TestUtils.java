package util;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.ParseFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
	
	/**
	 * Tests whether the clone of an object is equal to the original object.
	 * @param original Original object to clone
	 * @param <O> Type of the object
	 */
	public static <O extends Cloneable<O>> void testClone(O original) {
		O clone = original.clone();
		
		String className = original.getClass().getSimpleName();
		assertEquals(original, clone, String.format("clone of %s should be equal", className));
	}
	
	/**
	 * Tests whether a string can be parsed to an object.
	 * @param input Input string to parse
	 * @param expected Expected output of parse function
	 * @param parser Function to parse input string into an object
	 * @param <O> Type of the object
	 */
	public static <O> void testParse(String input, O expected, ParseFunction<O> parser) {
		O parsed = parser.parse(input);
		
		String className = expected.getClass().getSimpleName();
		assertEquals(expected, parsed, String.format("parse of string representation of %s should be equal", className));
	}
	
	/**
	 * Tests whether an object can be converted to a string and parsed back into an equal object.
	 * @param original Original object to convert to a string and then parse
	 * @param parser Function to parse string representation of object back into an object
	 * @param <O> Type of the object
	 */
	public static <O> void testToString(O original, ParseFunction<O> parser) {
	    String string = original.toString();
		O parsed = parser.parse(string);
		
		String className = original.getClass().getSimpleName();
		assertEquals(original, parsed, String.format("parsed result of string conversion of %s should equal original", className));
	}
	
}
