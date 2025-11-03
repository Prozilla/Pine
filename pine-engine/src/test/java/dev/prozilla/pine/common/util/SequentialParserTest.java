package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.util.parser.SequentialParser;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialParserTest {
	
	@Test
	void testReadingBetweenParentheses() {
		SequentialParser<Object> sequentialParser = new SequentialParser<>() {
			@Override
			public boolean parse(String input) {
				setInput(input);
				assertEquals("Test", readBetweenParentheses());
				return false;
			}
		};
		
		sequentialParser.parse("  (Test)  ");
	}
	
	@Test
	void testCheckingMethodCall() {
		SequentialParser<Object> sequentialParser = new SequentialParser<>() {
			@Override
			public boolean parse(String input) {
				setInput(input);
				assertTrue(isMethodCall("myMethod"));
				return false;
			}
		};
		
		sequentialParser.parse("  myMethod (true)  ");
	}
	
	@Test
	void testMethodCallParsing() {
		SequentialParser<Object> sequentialParser = new SequentialParser<>() {
			@Override
			public boolean parse(String input) {
				setInput(input);
				assertTrue(isMethodCall("myMethod"));
				assertEquals("true", readBetweenParentheses());
				return false;
			}
		};
		
		sequentialParser.parse("  myMethod (true)  ");
	}
	
	@Test
	void testReadWhile() {
		SequentialParser<Object> sequentialParser = new SequentialParser<>() {
			@Override
			public boolean parse(String input) {
				setInput(input);
				assertEquals("lowercase", readWhile(Character::isLowerCase));
				assertEquals("UPPERCASE", readWhile(Character::isUpperCase));
				return false;
			}
		};
		
		sequentialParser.parse("lowercaseUPPERCASE");
	}
	
}
