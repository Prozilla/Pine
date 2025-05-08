package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.AnimationDirection;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimatedPropertyTest {
	
	@Test
	void testAnimationDirections() {
		assertEquals(0.25f, AnimationDirection.NORMAL.get(0.25f, 1f));
		assertEquals(0.75f, AnimationDirection.REVERSE.get(0.25f, 1f));
		assertEquals(0.75f, AnimationDirection.ALTERNATE.get(1.25f, 1f));
	}
	
	@Test
	void testNormalAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, new AnimationCurve(1f, Easing.LINEAR, AnimationDirection.NORMAL));
		
		animatedProperty.restart();
		assertEquals(10f, animatedProperty.getValue(), "normal animation starts at start");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "normal animation progresses forward during first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(20f, animatedProperty.getValue(), "normal animation reaches end after first iteration");
	}
	
	@Test
	void testReverseAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, new AnimationCurve(1f, Easing.LINEAR, AnimationDirection.REVERSE));
		
		animatedProperty.restart();
		assertEquals(20f, animatedProperty.getValue(), "reverse animation starts at end");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "reverse animation progresses backwards during first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(10f, animatedProperty.getValue(), "reverse animation reaches start after first iteration");
	}
	
	@Test
	void testAlternatingAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, new AnimationCurve(1f, Easing.LINEAR, AnimationDirection.ALTERNATE));
		
		animatedProperty.restart();
		assertEquals(10f, animatedProperty.getValue(), "alternating animation starts at start");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "alternating animation progresses forward during first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(20f, animatedProperty.getValue(), "alternating animation reaches end after first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "alternating animation progresses backwards during second iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(10f, animatedProperty.getValue(), "alternating animation reaches start after second iteration");
	}
	
}
