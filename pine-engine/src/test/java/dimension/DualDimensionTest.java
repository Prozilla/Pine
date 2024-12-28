package dimension;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DualDimensionTest {

	@Test
	void testEquals() {
		DualDimension first = new DualDimension(new Dimension(20), new Dimension(30));
		DualDimension second = new DualDimension(new Dimension(20), new Dimension(30));
		
		assertEquals(first, second, "dual dimensions with equal dimension pairs should be equal");
	}
	
	@Test
	void testParsing() {
		String input = "20 30";
		DualDimension expected = new DualDimension(new Dimension(20), new Dimension(30));
		DualDimension actual = DualDimension.parse(input);
		
		assertEquals(expected, actual, "parsing of '20 30' should be equal to DualDimension(20, 30)");
	}
}
