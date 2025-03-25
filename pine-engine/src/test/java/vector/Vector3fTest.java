package vector;

import dev.prozilla.pine.common.math.vector.Vector3f;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestLoggingExtension;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Vector3fTest {
	
	@Test
	void testEquals() {
		assertEquals(mockVector3f(), mockVector3f(), "Vector3f with equal x, y and z values should be equal");
	}
	
	@Test
	void testParse() {
		TestUtils.testParse(mockVector3fString(), mockVector3f(), Vector3f::parse);
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(mockVector3f());
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(mockVector3f(), Vector3f::parse);
	}
	
	String mockVector3fString() {
		return "(0.5f, 1.5f, 2.5f)";
	}
	
	Vector3f mockVector3f() {
		return new Vector3f(0.5f, 1.5f, 2.5f);
	}
}
