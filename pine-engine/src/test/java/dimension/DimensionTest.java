package dimension;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.Unit;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DimensionTest {

	@Test
	void testEquals() {
		Dimension first = new Dimension(50, Unit.VIEWPORT_WIDTH);
		Dimension second = new Dimension(50, Unit.VIEWPORT_WIDTH);
		
		assertEquals(first, second, "dimensions with equal value and equal unit should be equal");
	}
	
	@Test
	void testParsing() {
		String input = "50vw";
		Dimension expected = new Dimension(50, Unit.VIEWPORT_WIDTH);
		DimensionBase actual = Dimension.parse(input);
		
		assertEquals(expected, actual, "parsing of '50vw' should be equal to Dimension(50, VIEWPORT_WIDTH)");
	}

	@Test
	void testParsingFunction() {
		String input = "clamp(10vh, 5px, 10px)";
		DimensionBase expected = Dimension.clamp(new Dimension(10, Unit.VIEWPORT_HEIGHT), new Dimension(5), new Dimension(10));
		DimensionBase actual = Dimension.parse(input);
		
		assertEquals(expected, actual, "parsing of 'clamp(10vh, 5px, 10px)' should be equal to Dimension.clamp");
	}
}
