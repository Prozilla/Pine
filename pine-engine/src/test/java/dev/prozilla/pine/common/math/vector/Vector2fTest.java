package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Vector2fTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector2f(), mockVector2f(), "Vector2f with equal x and y values should be equal");
	}
	
	@Test
	void testParse() {
		TestUtils.testParse(mockVector2fString(), mockVector2f(), new Vector2f.Parser());
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector2f());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector2f(), new Vector2f.Parser());
	}
	
	String mockVector2fString() {
		return "(0.5f, 1.5f)";
	}
	
	Vector2f mockVector2f() {
		return new Vector2f(0.5f, 1.5f);
	}
}
