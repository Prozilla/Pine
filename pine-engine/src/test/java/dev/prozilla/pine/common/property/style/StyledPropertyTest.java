package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.AnimationCurveParser;
import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StyledPropertyTest {
	
	@Test
	void testAnimationCurveParser() {
		TestUtils.testParser("0.25s ease-in-out-quad", new AnimationCurve(0.25f, Easing.EASE_IN_OUT_QUAD), new AnimationCurveParser());
	}
	
}
