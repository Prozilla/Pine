package dimension;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.Unit;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestExtension;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DimensionTest {

	@Test
	void testEquals() {
		Dimension first = new Dimension(50, Unit.VIEWPORT_WIDTH);
		Dimension second = new Dimension(50, Unit.VIEWPORT_WIDTH);
		
		assertEquals(first, second, "dimensions with equal value and equal unit should be equal");
	}
	
	@Test
	void testParse() {
		TestUtils.testParse("50vw", new Dimension(50, Unit.VIEWPORT_WIDTH), Dimension::parse);
		TestUtils.testParse("clamp(10vh, 5px, 10px)", Dimension.clamp(new Dimension(10, Unit.VIEWPORT_HEIGHT), new Dimension(5), new Dimension(10)), Dimension::parse);
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(new Dimension(50, Unit.VIEWPORT_WIDTH));
	}
	
	@Test
	void testToString() {
		TestUtils.testToString(new Dimension(50, Unit.VIEWPORT_WIDTH), Dimension::parse);
	}
}
