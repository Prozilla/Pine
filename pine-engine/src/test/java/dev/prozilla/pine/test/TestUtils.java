package dev.prozilla.pine.test;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {
	
	/**
	 * Tests whether the clone of an object is equal to the original object
	 * and whether the original object is not equal to null.
	 * @param original Original object to clone
	 * @param <O> Type of the object
	 */
	public static <O extends Cloneable<O>> void testCloneable(O original) {
		O clone = original.clone();
		
		String className = original.getClass().getSimpleName();
		assertEquals(original, clone, String.format("clone of %s should be equal", className));
		assertFalse(original.equals(null), "Object should not equal null");
	}
	
	/**
	 * Tests whether a string can be parsed to an object.
	 * @param input Input string to parse
	 * @param expected Expected output of parse function
	 * @param parser The parser to use
	 * @param <O> Type of the object
	 */
	public static <O> void testParser(String input, O expected, Parser<O> parser) {
		if (!parser.parse(input)) {
			fail(parser.getError());
			return;
		}
		
		String className = expected.getClass().getSimpleName();
		assertNull(parser.getError());
		assertEquals(expected, parser.getResult(), String.format("parsed result of string representation of %s should be equal", className));
	}
	
	public static <O> void testParserFailure(String input, Parser<O> parser) {
		testParserFailure(input, null, parser);
	}
	
	public static <O> void testParserFailure(String input, String expectedError, Parser<O> parser) {
		if (parser.parse(input)) {
			fail("Parser should fail, but succeeded with result: " + parser.getResult());
			return;
		}
		
		String actualError = parser.getError();
		assertNotNull(actualError);
		if (expectedError != null) {
			assertTrue(actualError.toLowerCase().contains(expectedError.toLowerCase()), String.format("error message of parser should contain '%s', but was '%s'", expectedError, actualError));
		}
	}
	
	/**
	 * Tests whether an object can be converted to a string and parsed back into an equal object.
	 * @param original Original object to convert to a string and then parse
	 * @param parser The parser to use
	 * @param <O> Type of the object
	 */
	public static <O> void testToString(O original, Parser<O> parser) {
	    String string = original.toString();
		if (!parser.parse(string)) {
			fail(parser.getError());
			return;
		}
		
		String className = original.getClass().getSimpleName();
		assertEquals(original, parser.getResult(), String.format("parsed result of string conversion of %s should equal original", className));
	}
	
	/**
	 * Asserts that the given string is not {@code null} or blank.
	 */
	public static void assertNonBlankString(String string) {
		assertNotNull(string);
		assertFalse(string.isBlank(), "string should not be blank");
	}
	
	public static void assertEndsWith(String string, String suffix) {
		assertNotNull(string);
		assertTrue(string.endsWith(suffix), String.format("string should end with '%s', but was: %s", suffix, string));
	}
	
	public static void assertContains(String string, String substring) {
		assertNotNull(string);
		assertTrue(string.contains(substring), String.format("string should contain '%s', but was: %s", substring, string));
	}
	
	public static void assertContainsOnce(String string, String substring) {
		assertNotNull(string);
		assertTrue(StringUtils.containsOnce(string, substring), String.format("string should contain '%s' once, but was: %s", substring, string));
	}
	
}
