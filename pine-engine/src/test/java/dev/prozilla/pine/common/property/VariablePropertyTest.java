package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import dev.prozilla.pine.test.TestPerformanceExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({TestPerformanceExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VariablePropertyTest {
	
	private static final int ITERATIONS = 10_000_000;
	private static final float ANIMATION_DURATION = 10f;
	private static final float EPSILON = 0.1f;
	
	/** Array of properties to avoid garbage collection */
	@SuppressWarnings("unchecked")
	private static final VariableProperty<Float>[] properties = new VariableProperty[3];
	
	@Test
	@Tag("performance")
	void testFixedFloatPerformance() {
		VariableProperty<Float> property = new FixedProperty<>(5f);
		properties[0] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertEquals(5, value, "value of FixedProperty stays unchanged");
	}
	
	@Test
	@Tag("performance")
	void testDynamicFloatPerformance() {
		VariableProperty<Float> property = new RandomFloatProperty(0f, 10f);
		properties[1] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertTrue(0f <= value && value <= 10f, "value of RandomFloatProperty must be within its range of values");
	}
	
	@Test
	@Tag("performance")
	void testAnimatedFloatPerformance() {
		VariableProperty<Float> property = new AnimatedFloatProperty(0f, 10f, new AnimationCurve(ANIMATION_DURATION, Easing.EASE_IN_OUT_QUAD));
		properties[2] = property;
		
		float value = simulateFloatPropertyUsage(property);
		
		assertEquals(10f, value, "value of AnimatedFloatProperty must be the end value after the animation has finished");
	}
	
	private float simulateFloatPropertyUsage(VariableProperty<Float> property) {
		AnimatedFloatProperty animatedProperty = property instanceof  AnimatedFloatProperty animatedFloatProperty ? animatedFloatProperty : null;
		
		if (animatedProperty != null) {
			animatedProperty.restart();
		}
		
		float value = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			if (animatedProperty != null) {
				animatedProperty.update( (1f + EPSILON) * ANIMATION_DURATION  / ITERATIONS);
			}
			value = property.getValue();
		}
		
		return value;
	}
	
}
