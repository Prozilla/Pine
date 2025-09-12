package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EasingParserTest {
	
	@Test
	void testParse() {
		EasingParser easingParser = new EasingParser();
		TestUtils.testParser("ease", Easing.EASE, easingParser);
		TestUtils.testParser("cubic-bezier(0, 0, 1, 1)", new CubicBezierEasing(0, 0, 1, 1), easingParser);
	}
	
}
