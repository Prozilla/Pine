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
	void testParser() {
		EasingParser easingParser = new EasingParser();
		TestUtils.testParser("ease", Easing.EASE, easingParser);
		TestUtils.testParser("ease-in-out", Easing.EASE_IN_OUT, easingParser);
		TestUtils.testParser("ease-in-out-quad", Easing.EASE_IN_OUT_QUAD, easingParser);
		TestUtils.testParser("linear", Easing.LINEAR, easingParser);
		TestUtils.testParser("EASE", Easing.EASE, easingParser);
		TestUtils.testParser("cubic-bezier(0, 0, 1, 1)", new CubicBezierEasing(0, 0, 1, 1), easingParser);
		TestUtils.testParser("CUBIC-BEZIER(0, 0, 1, 1)", new CubicBezierEasing(0, 0, 1, 1), easingParser);
		TestUtils.testParser("cubic-bezier(0.0f, 0.25f, 0.75f, 1f)", new CubicBezierEasing(0, 0.25f, 0.75f, 1), easingParser);
		TestUtils.testParser("steps(5)", new StepEasing(5), easingParser);
		TestUtils.testParser("STEPS(5)", new StepEasing(5), easingParser);
		TestUtils.testParser("steps(5, end)", new StepEasing(5, false), easingParser);
		TestUtils.testParser("steps(5, start)", new StepEasing(5, true), easingParser);
	}
	
	@Test
	void testParserFailure() {
		EasingParser easingParser = new EasingParser();
		TestUtils.testParserFailure("e", easingParser);
		TestUtils.testParserFailure("eeeeease", easingParser);
		TestUtils.testParserFailure("cubic-bezier()", "Missing arguments", easingParser);
		TestUtils.testParserFailure("steps()", "Missing arguments", easingParser);
		TestUtils.testParserFailure("steps(foo)", "Invalid argument", easingParser);
		TestUtils.testParserFailure("steps(5, foo)", "Invalid argument", easingParser);
		TestUtils.testParserFailure("steps(5, foo, bar)", "Invalid argument", easingParser);
	}
	
}
