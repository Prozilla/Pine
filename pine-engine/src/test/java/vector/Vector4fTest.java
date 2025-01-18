package vector;

import dev.prozilla.pine.common.math.vector.Vector4f;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestExtension;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Vector4fTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector4f(), mockVector4f(), "Vector4f with equal x, y, z and w values should be equal");
	}
	
	@Test
	void testParse() {
		TestUtils.testParse(mockVector4fString(), mockVector4f(), Vector4f::parse);
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector4f());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector4f(), Vector4f::parse);
	}
	
	String mockVector4fString() {
		return "(0.5f, 1.5f, 2.5f, 3.5f)";
	}
	
	Vector4f mockVector4f() {
		return new Vector4f(0.5f, 1.5f, 2.5f, 3.5f);
	}
}
