package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CircleTest {
	
	@Test
	void testClone() {
		TestUtils.testCloneable(mockCircle());
	}
	
	Circle mockCircle() {
		return new Circle(new Vector2f(1, 2), 4f);
	}
	
}
