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
public class Vector4fTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector4f(), mockVector4f(), "Vector4f with equal x, y, z and w values should be equal");
	}
	
	@Test
	void testParser() {
		Vector4f.Parser parser = new Vector4f.Parser();
		TestUtils.testParser(mockVector4fString(), mockVector4f(), parser);
		TestUtils.testParser("(0.5f, 1.5f)", new Vector4f(0.5f, 1.5f, 0.5f, 1.5f), parser);
		TestUtils.testParser("0.5f, 1.5f", new Vector4f(0.5f, 1.5f, 0.5f, 1.5f), parser);
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector4f());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector4f(), new Vector4f.Parser());
	}
	
	String mockVector4fString() {
		return "(0.5f, 1.5f, 2.5f, 3.5f)";
	}
	
	Vector4f mockVector4f() {
		return new Vector4f(0.5f, 1.5f, 2.5f, 3.5f);
	}
}
