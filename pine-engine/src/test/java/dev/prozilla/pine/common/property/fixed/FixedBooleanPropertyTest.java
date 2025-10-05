package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.property.random.RandomBooleanProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.prozilla.pine.test.TestUtils.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FixedBooleanPropertyTest {
	
	@Test
	void testAnd() {
		BooleanProperty booleanProperty = new RandomBooleanProperty();
		
		assertEquals(booleanProperty, BooleanProperty.TRUE.and(booleanProperty));
		assertEquals(booleanProperty, booleanProperty.and(BooleanProperty.TRUE));
		assertEquals(BooleanProperty.FALSE, BooleanProperty.FALSE.and(booleanProperty));
		assertEquals(BooleanProperty.FALSE, booleanProperty.and(BooleanProperty.FALSE));
	}
	
	@Test
	void testOr() {
		BooleanProperty booleanProperty = new RandomBooleanProperty();
		
		assertEquals(BooleanProperty.TRUE, BooleanProperty.TRUE.or(booleanProperty));
		assertEquals(BooleanProperty.TRUE, booleanProperty.or(BooleanProperty.TRUE));
		assertEquals(booleanProperty, BooleanProperty.FALSE.or(booleanProperty));
		assertEquals(booleanProperty, booleanProperty.or(BooleanProperty.FALSE));
	}
	
	@Test
	void testNot() {
		assertEquals(BooleanProperty.TRUE, BooleanProperty.FALSE.not());
		assertEquals(BooleanProperty.FALSE, BooleanProperty.TRUE.not());
	}
	
	@Test
	void testXor() {
		BooleanProperty booleanProperty = new RandomBooleanProperty();
		
		assertEquals(booleanProperty, BooleanProperty.FALSE.xor(booleanProperty));
		assertEquals(booleanProperty, booleanProperty.xor(BooleanProperty.FALSE));
	}
	
	@Test
	void testValue() {
		assertEquals(Boolean.TRUE, BooleanProperty.TRUE.getValue());
		assertEquals(Boolean.FALSE, BooleanProperty.FALSE.getValue());
		assertTrue(BooleanProperty.TRUE.get());
		assertFalse(BooleanProperty.FALSE.get());
	}
	
	@Test
	void testToStringProperty() {
		StringProperty stringPropertyTrue = BooleanProperty.TRUE.toStringProperty();
		StringProperty stringPropertyFalse = BooleanProperty.FALSE.toStringProperty();
		
		assertInstanceOf(stringPropertyTrue, FixedStringProperty.class);
		assertInstanceOf(stringPropertyFalse, FixedStringProperty.class);
		
		assertEquals("true", stringPropertyTrue.getValue());
		assertEquals("false", stringPropertyFalse.getValue());
	}
	
}
