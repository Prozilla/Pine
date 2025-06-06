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
public class Vector4iTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector4i(), mockVector4i(), "Vector4i with equal x, y, z and w values should be equal");
	}
	
	@Test
	void testParse() {
		TestUtils.testParse(mockVector4iString(), mockVector4i(), Vector4i::parse);
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector4i());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector4i(), Vector4i::parse);
	}
	
	String mockVector4iString() {
		return "(20, 30, 40, 50)";
	}
	
	Vector4i mockVector4i() {
		return new Vector4i(20, 30, 40, 50);
	}
}
