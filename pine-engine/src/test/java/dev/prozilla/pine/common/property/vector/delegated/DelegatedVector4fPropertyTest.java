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
public class DelegatedVector4fPropertyTest {
	
	@Test
	void testNonNull() {
		DelegatedVector4fProperty property = mockProperty();
		
		assertTrue(property.isNotNull());
		assertEquals(BooleanProperty.TRUE, property.isNotNullProperty());
		assertNotNull(property.getValue());
	}
	
	@Test
	void testValues() {
		DelegatedVector4fProperty property = mockProperty();
		
		assertEquals(1, property.getX());
		assertEquals(2, property.getY());
		assertEquals(3, property.getZ());
		assertEquals(4, property.getW());
	}
	
	@Test
	void testDerivedProperties() {
		FloatProperty x = new FixedFloatProperty(1);
		FloatProperty y = new FixedFloatProperty(2);
		FloatProperty z = new FixedFloatProperty(3);
		FloatProperty w = new FixedFloatProperty(4);
		DelegatedVector4fProperty property = new DelegatedVector4fProperty(x, y, z, w);
		
		assertEquals(x, property.xProperty());
		assertEquals(y, property.yProperty());
		assertEquals(z, property.zProperty());
		assertEquals(w, property.wProperty());
	}
	
	DelegatedVector4fProperty mockProperty() {
		FloatProperty x = new FixedFloatProperty(1);
		FloatProperty y = new FixedFloatProperty(2);
		FloatProperty z = new FixedFloatProperty(3);
		FloatProperty w = new FixedFloatProperty(4);
		return new DelegatedVector4fProperty(x, y, z, w);
	}
	
}
