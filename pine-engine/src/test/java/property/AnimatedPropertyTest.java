package property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimatedFloatProperty;
import dev.prozilla.pine.common.property.animated.AnimationDirection;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestLoggingExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimatedPropertyTest {
	
	@Test
	void testNormalAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, 1f, Easing.LINEAR, AnimationDirection.NORMAL);
		
		animatedProperty.restart();
		assertEquals(10f, animatedProperty.getValue(), "normal animation starts at start");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "normal animation progresses forward during first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(20f, animatedProperty.getValue(), "normal animation reaches end after first iteration");
	}
	
	@Test
	void testReverseAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, 1f, Easing.LINEAR, AnimationDirection.REVERSE);
		
		animatedProperty.restart();
		assertEquals(20f, animatedProperty.getValue(), "reverse animation starts at end");
		
		animatedProperty.update(0.5f);
		assertEquals(15f, animatedProperty.getValue(), "reverse animation progresses backwards during first iteration");
		
		animatedProperty.update(0.5f);
		assertEquals(10f, animatedProperty.getValue(), "reverse animation reaches start after first iteration");
	}
	
	@Test
	void testAlternatingAnimatedFloatProperty() {
		AnimatedFloatProperty animatedProperty = new AnimatedFloatProperty(10f, 20f, 1f, Easing.LINEAR, AnimationDirection.ALTERNATE);
		
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
