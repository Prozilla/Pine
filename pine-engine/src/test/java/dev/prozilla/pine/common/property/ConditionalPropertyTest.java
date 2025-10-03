package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.mutable.MutableBooleanProperty;
import dev.prozilla.pine.common.property.mutable.SimpleMutableBooleanProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConditionalPropertyTest {
	
	@Test
	void testValue() {
		MutableBooleanProperty condition = mockCondition();
		
		ConditionalProperty<Integer> property = new ConditionalProperty<>(condition, 10, 20);
		
		assertEquals(20, property.getValue());
		assertFalse(property.isTrue());
		assertTrue(property.isFalse());
		
		condition.set(true);
		
		assertEquals(10, property.getValue());
		assertTrue(property.isTrue());
		assertFalse(property.isFalse());
		
		condition.toggle();
		assertTrue(property.isFalse());
	}
	
	MutableBooleanProperty mockCondition() {
		return new SimpleMutableBooleanProperty(false);
	}
	
}
