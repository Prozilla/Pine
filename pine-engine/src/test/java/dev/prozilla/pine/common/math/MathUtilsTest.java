package dev.prozilla.pine.common.math;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MathUtilsTest {
	
	@Test
	void testMin() {
		float[] input = new float[]{ 1, 2, 3, -4 };
		assertEquals(-4, MathUtils.min(input));
	}
	
	@Test
	void testMinWithoutArgs() {
		assertEquals(Float.MAX_VALUE, MathUtils.min());
	}
	
	@Test
	void testMax() {
		float[] input = new float[]{ 1, 2, 3, -4 };
		assertEquals(3, MathUtils.max(input));
	}
	
	@Test
	void testMaxWithoutArgs() {
		assertEquals(-Float.MAX_VALUE, MathUtils.max());
	}
	
	@Test
	void testNormalize() {
		double[] input = new double[]{ 1, 2, 3, -4 };
		MathUtils.normalize(input);
		assertEquals(1, MathUtils.max(input));
	}
	
	@Test
	void testRemap() {
		assertEquals(1, MathUtils.remap(0.5f, 0, 1, 0, 2));
	}
	
}
