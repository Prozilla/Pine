package property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.adaptive.AdaptiveFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestPerformanceExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({TestPerformanceExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdaptivePropertyTest {
	
	private static final int ITERATIONS = 10_000_000;
	private static final float ANIMATION_DURATION = 10f;
	private static final float EPSILON = 0.1f;
	
	/** Array of properties to avoid garbage collection */
	private static final AdaptiveFloatProperty[] properties = new AdaptiveFloatProperty[3];
	
	@Test
	@Tag("performance")
	void testFixedFloatPerformance() {
		AdaptiveFloatProperty property = new AdaptiveFloatProperty(5f);
		properties[0] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertEquals(5, value, "value of fixed AdaptiveFloatProperty stays unchanged");
	}
	
	@Test
	@Tag("performance")
	void testDynamicFloatPerformance() {
		AdaptiveFloatProperty property = new AdaptiveFloatProperty(new RandomFloatProperty(0f, 10f));
		properties[1] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertTrue(0f <= value && value <= 10f, "value of dynamic AdaptiveFloatProperty must be within its range of values");
	}
	
	@Test
	@Tag("performance")
	void testAnimatedFloatPerformance() {
		AdaptiveFloatProperty property = new AdaptiveFloatProperty(new AnimatedFloatProperty(0f, 10f, new AnimationCurve(ANIMATION_DURATION, Easing.EASE_IN_OUT__QUAD)));
		properties[2] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertEquals(10f, value, "value of animated AdaptiveFloatProperty must be the end value after the animation has finished");
	}
	
	private float simulateFloatPropertyUsage(AdaptiveFloatProperty property) {
		property.restartAnimation();
		
		float value = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			property.updateAnimation( (1f + EPSILON) * ANIMATION_DURATION  / ITERATIONS);
			value = property.getPrimitiveValue();
		}
		
		return value;
	}
	
}
