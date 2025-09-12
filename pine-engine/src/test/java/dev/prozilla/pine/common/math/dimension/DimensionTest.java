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
public class DimensionTest {

	@Test
	void testEquals() {
		Dimension first = new Dimension(50, Unit.VIEWPORT_WIDTH);
		Dimension second = new Dimension(50, Unit.VIEWPORT_WIDTH);
		
		assertEquals(first, second, "dimensions with equal value and equal unit should be equal");
	}
	
	@Test
	void testParser() {
		DimensionParser dimensionParser = new DimensionParser();
		TestUtils.testParser("50vw", new Dimension(50, Unit.VIEWPORT_WIDTH), dimensionParser);
		TestUtils.testParser("clamp(10vh, 5px, 10px)", Dimension.clamp(new Dimension(10, Unit.VIEWPORT_HEIGHT), new Dimension(5), new Dimension(10)), dimensionParser);
		TestUtils.testParser(".75px", new Dimension(0.75f), dimensionParser);
		TestUtils.testParser("0.75px", new Dimension(0.75f), dimensionParser);
	}
	
	@Test
	void testClone() {
		TestUtils.testCloneable(new Dimension(50, Unit.VIEWPORT_WIDTH));
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(new Dimension(50, Unit.VIEWPORT_WIDTH), new DimensionParser());
	}
}
