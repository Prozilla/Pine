package resource;

import dev.prozilla.pine.common.system.resource.Color;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestExtension;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestExtension.class)
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
		
		assertEquals(a, new Color(0.5f, 0.5f, 0.5f), "colors should mix correctly");
	}
	
}
