package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DualDimensionTest {

	@Test
	void testEquals() {
		DualDimension first = new DualDimension(new Dimension(20), new Dimension(30));
		DualDimension second = new DualDimension(new Dimension(20), new Dimension(30));
		
		assertEquals(first, second, "dual dimensions with equal dimension pairs should be equal");
	}
	
	@Test
	void testParser() {
		TestUtils.testParser("20 30", new DualDimension(new Dimension(20), new Dimension(30)), new DualDimensionParser());
	}
	
	@Test
	void testClone() {
		TestUtils.testCloneable(new DualDimension(new Dimension(20), new Dimension(30)));
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(new DualDimension(new Dimension(20), new Dimension(30)), new DualDimensionParser());
	}
}
