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
public class ColourTest {
	
	@Test
	void testEqualsColor() {
		assertEquals(new Color(0.25f, 0.5f, 0.75f, 1f), new Colour(0.25f, 0.5f, 0.75f, 1f));
		assertEquals(new Color(0.25f, 0.5f, 0.75f), new Colour(0.25f, 0.5f, 0.75f));
		assertEquals(Color.decode("#FFF"), Colour.decode("#FFF"));
		assertEquals(Color.rebeccaPurple(), Colour.rebeccaPurple());
		assertEquals(Color.hsl(30, 70, 50), Colour.hsl(30, 70, 50));
		assertEquals(Color.parse("rgba(0.25, 0.75, 0.5, 1)"), Colour.parse("rgba(0.25, 0.75, 0.5, 1)"));
	}
	
	@Test
	void testClone() {
		TestUtils.testCloneable(new Colour(0.25f, 0.5f, 0.75f));
	}
	
	@Test
	void testParser() {
		ColourParser parser = new ColourParser();
		TestUtils.testParser("rgba(0.25, 0.75, 0.5, 1)", new Colour(0.25f, 0.75f, 0.5f), parser);
		TestUtils.testParser("#FFF", new Colour(1f, 1f, 1f), parser);
		TestUtils.testParser("rebeccapurple", Colour.rebeccaPurple(), parser);
	}
	
}
