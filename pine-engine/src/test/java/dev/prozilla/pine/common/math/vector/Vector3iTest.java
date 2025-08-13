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
public class Vector3iTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector3i(), mockVector3i(), "Vector3i with equal x, y and z values should be equal");
	}
	
	@Test
	void testParser() {
		TestUtils.testParser(mockVector3iString(), mockVector3i(), new Vector3i.Parser());
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector3i());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector3i(), new Vector3i.Parser());
	}
	
	String mockVector3iString() {
		return "(20, 30, 40)";
	}
	
	Vector3i mockVector3i() {
		return new Vector3i(20, 30, 40);
	}
}
