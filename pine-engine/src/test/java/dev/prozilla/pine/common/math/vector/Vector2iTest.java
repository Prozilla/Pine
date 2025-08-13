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
public class Vector2iTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector2i(), mockVector2i(), "Vector2i with equal x and y values should be equal");
	}
	
	@Test
	void testParser() {
		TestUtils.testParser(mockVector2iString(), mockVector2i(), new Vector2i.Parser());
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector2i());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector2i(), new Vector2i.Parser());
	}
	
	String mockVector2iString() {
		return "(20, 30)";
	}
	
	Vector2i mockVector2i() {
		return new Vector2i(20, 30);
	}
}
