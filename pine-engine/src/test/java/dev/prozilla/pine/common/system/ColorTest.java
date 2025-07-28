package dev.prozilla.pine.common.system;

import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ColorTest {
	
	@Test
	void testEquals() {
		Color first = new Color(0.25f, 0.5f, 0.75f, 1f);
		Color second = new Color(0.25f, 0.5f, 0.75f, 1f);
		
		assertEquals(first, second, "colors with equal values should be equal");
	}
	
	@Test
	void testClone() {
		TestUtils.testClone(new Color(0.25f, 0.5f, 0.75f));
	}
	
	@Test
	void testMix() {
		Color a = Color.white();
		Color b = Color.black();
		
		a.mix(b);
		
		assertEquals(new Color(0.5f, 0.5f, 0.5f), a, "colors should mix correctly");
	}
	
	@Test
	void testParser() {
		ColorParser colorParser = new ColorParser();
		TestUtils.testParser("rgba(0.25, 0.75, 0.5, 1)", new Color(0.25f, 0.75f, 0.5f), colorParser);
		TestUtils.testParser("#FFF", new Color(1f, 1f, 1f), colorParser);
		TestUtils.testParser("rebeccapurple", Color.rebeccaPurple(), colorParser);
	}
	
}
