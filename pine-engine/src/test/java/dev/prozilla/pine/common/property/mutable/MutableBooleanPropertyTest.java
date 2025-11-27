package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.prozilla.pine.test.TestUtils.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MutableBooleanPropertyTest {
	
	@Test
	void testDerivedProperties() {
		MutableBooleanProperty booleanPropertyA = mockProperty(true);
		MutableBooleanProperty booleanPropertyB = mockProperty(false);
		
		assertInstanceOf(booleanPropertyA.and(booleanPropertyB), BooleanProperty.class);
		assertInstanceOf(booleanPropertyA.or(booleanPropertyB), BooleanProperty.class);
		assertInstanceOf(booleanPropertyA.xor(booleanPropertyB), BooleanProperty.class);
		assertInstanceOf(booleanPropertyA.not(), BooleanProperty.class);
		assertEquals(BooleanProperty.TRUE, booleanPropertyA.isNotNullProperty());
		assertEquals(booleanPropertyA, BooleanProperty.TRUE.ifElse(booleanPropertyA, booleanPropertyB));
		assertEquals(booleanPropertyB, BooleanProperty.FALSE.ifElse(booleanPropertyA, booleanPropertyB));
		assertEquals(BooleanProperty.FALSE, BooleanProperty.TRUE.not());
	}
	
	@Test
	void testMutations() {
		MutableBooleanProperty booleanProperty = mockProperty(true);
		assertTrue(booleanProperty.get());
		
		booleanProperty.set(false);
		assertFalse(booleanProperty.get());
		
		booleanProperty.toggle();
		assertTrue(booleanProperty.get());
		
		assertTrue(booleanProperty.swap(false));
		assertFalse(booleanProperty.get());
	}
	
	@Test
	void testView() {
		MutableBooleanProperty booleanProperty = mockProperty(true);
		BooleanProperty view = booleanProperty.viewProperty();
		assertTrue(view.get(), "view of boolean property should inherit value");
		if (view instanceof MutableBooleanProperty mutableView) {
			mutableView.set(false);
		}
		assertTrue(booleanProperty.get(), "boolean property should not be mutable via view");
		assertTrue(view.get(), "view of boolean property should not be mutable");
		booleanProperty.set(false);
		assertFalse(view.get(), "view of boolean property should inherit value");
	}
	
	MutableBooleanProperty mockProperty(boolean initialValue) {
		return new SimpleMutableBooleanProperty(initialValue);
	}
	
}
