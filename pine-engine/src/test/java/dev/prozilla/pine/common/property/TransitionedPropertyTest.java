package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedIntProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransitionedPropertyTest {
	
	@Test
	void testTransitionedIntProperty() {
		TransitionedIntProperty transitionedProperty = new TransitionedIntProperty(5, new AnimationCurve(1f));
		
		transitionedProperty.restart();
		assertEquals(5, transitionedProperty.getValue(), "transition starts at start");
		
		transitionedProperty.transitionTo(15);
		assertEquals(5, transitionedProperty.getValue(), "transition stays the same until time passes");
		
		transitionedProperty.update(0.5f);
		assertEquals(10, transitionedProperty.getValue(), "transition moves towards target value at speed of animation curve");
		
		transitionedProperty.update(0.5f);
		assertEquals(15, transitionedProperty.getValue(), "transition reaches target value after duration of animation curve");
		
		transitionedProperty.update(0.5f);
		assertEquals(15, transitionedProperty.getValue(), "transition stays the same after target value has been reached until new target value is set");
		
		transitionedProperty.transitionTo(10);
		transitionedProperty.update(1f);
		assertEquals(10, transitionedProperty.getValue(), "transition reaches next target value after duration of animation curve");
	}
	
}
