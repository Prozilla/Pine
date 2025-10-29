package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DelegatedVector2fPropertyTest {
	
	@Test
	void testNonNull() {
		DelegatedVector2fProperty property = mockProperty();
		
		assertTrue(property.isNotNull());
		assertEquals(BooleanProperty.TRUE, property.isNotNullProperty());
		assertNotNull(property.getValue());
	}
	
	@Test
	void testValues() {
		DelegatedVector2fProperty property = mockProperty();
		
		assertEquals(1, property.getX());
		assertEquals(2, property.getY());
	}
	
	@Test
	void testDerivedProperties() {
		FloatProperty x = new FixedFloatProperty(1);
		FloatProperty y = new FixedFloatProperty(2);
		DelegatedVector2fProperty property = new DelegatedVector2fProperty(x, y);
		
		assertEquals(x, property.xProperty());
		assertEquals(y, property.yProperty());
	}
	
	DelegatedVector2fProperty mockProperty() {
		FloatProperty x = new FixedFloatProperty(1);
		FloatProperty y = new FixedFloatProperty(2);
		return new DelegatedVector2fProperty(x, y);
	}
	
}
