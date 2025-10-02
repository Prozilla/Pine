package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.mutable.MutableObjectProperty;
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
		MutableObjectProperty<Boolean> condition = mockCondition();
		
		ConditionalProperty<Integer> property = new ConditionalProperty<>(condition, 10, 20);
		
		assertEquals(20, property.getValue());
		assertFalse(property.isTrue());
		assertTrue(property.isFalse());
		
		condition.setValue(true);
		
		assertEquals(10, property.getValue());
		assertTrue(property.isTrue());
		assertFalse(property.isFalse());
	}
	
	MutableObjectProperty<Boolean> mockCondition() {
		return new MutableObjectProperty<>(false);
	}
	
}
