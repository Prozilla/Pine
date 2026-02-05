package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import dev.prozilla.pine.common.property.fixed.FixedIntProperty;
import dev.prozilla.pine.common.property.mutable.MutableIntProperty;
import dev.prozilla.pine.common.property.mutable.SimpleMutableIntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.prozilla.pine.test.TestUtils.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComparedIntPropertyTest {
	
	@Test
	void testCompareWithValue() {
		MutableIntProperty property = mockMutableIntProperty();
		property.set(0);
		
		assertGreaterThan(property, -1);
		assertLessThan(property, 1);
		assertEqualTo(property, 0);
	}
	
	@Test
	void testCompareFixedProperties() {
		IntProperty a = new FixedIntProperty(0);
		IntProperty b = new FixedIntProperty(1);
		
		assertInstanceOf(a.compareWith(1), FixedIntProperty.class);
		assertInstanceOf(a.compareWith(b), FixedIntProperty.class);
		assertInstanceOf(a.compareWith(IntComparator.naturalOrder(), 1), FixedIntProperty.class);
		assertInstanceOf(a.compareWith(IntComparator.naturalOrder(), b), FixedIntProperty.class);
		assertInstanceOf(a.isGreaterThanProperty(1), FixedBooleanProperty.class);
		assertInstanceOf(a.isGreaterThanProperty(b), FixedBooleanProperty.class);
		assertInstanceOf(a.isLessThanProperty(1), FixedBooleanProperty.class);
		assertInstanceOf(a.isLessThanProperty(b), FixedBooleanProperty.class);
		assertInstanceOf(a.isGreaterThanOrEqualToProperty(1), FixedBooleanProperty.class);
		assertInstanceOf(a.isGreaterThanOrEqualToProperty(b), FixedBooleanProperty.class);
		assertInstanceOf(a.isLessThanOrEqualToProperty(1), FixedBooleanProperty.class);
		assertInstanceOf(a.isLessThanOrEqualToProperty(b), FixedBooleanProperty.class);
	}
	
	private static void assertGreaterThan(IntProperty property, int value) {
		assertTrue(property.get() > value, "property should have a greater value");
		
		assertGreaterThan(property, value, true);
		assertLessThan(property, value, false);
		assertGreaterThanOrEqualTo(property, value, true);
		assertLessThanOrEqualTo(property, value, false);
	}
	
	private static void assertLessThan(IntProperty property, int value) {
		assertTrue(property.get() < value, "property should have a lesser value");
		
		assertGreaterThan(property, value, false);
		assertLessThan(property, value, true);
		assertGreaterThanOrEqualTo(property, value, false);
		assertLessThanOrEqualTo(property, value, true);
	}
	
	private static void assertEqualTo(IntProperty property, int value) {
		assertEquals(value, property.get(), "property should have an equal value");
		assertTrue(property.has(value), "property should have the given value");
		
		assertGreaterThan(property, value, false);
		assertLessThan(property, value, false);
		assertGreaterThanOrEqualTo(property, value, true);
		assertLessThanOrEqualTo(property, value, true);
	}
	
	private static void assertGreaterThan(IntProperty property, int value, boolean expected) {
		assertEquals(expected, property.isGreaterThan(value));
		assertEquals(expected, property.isGreaterThanProperty(value).get());
		assertEquals(expected, property.isGreaterThanProperty(() -> value).get());
		assertEquals(expected, property.compareWith(value).get() > 0);
		assertEquals(expected, property.compareWith(() -> value).get() > 0);
	}
	
	private static void assertLessThan(IntProperty property, int value, boolean expected) {
		assertEquals(expected, property.isLessThan(value));
		assertEquals(expected, property.isLessThanProperty(value).get());
		assertEquals(expected, property.isLessThanProperty(() -> value).get());
		assertEquals(expected, property.compareWith(value).get() < 0);
		assertEquals(expected, property.compareWith(() -> value).get() < 0);
	}
	
	private static void assertGreaterThanOrEqualTo(IntProperty property, int value, boolean expected) {
		assertEquals(expected, property.isGreaterThanOrEqualTo(value));
		assertEquals(expected, property.isGreaterThanOrEqualToProperty(value).get());
		assertEquals(expected, property.isGreaterThanOrEqualToProperty(() -> value).get());
		assertEquals(expected, property.compareWith(value).get() >= 0);
		assertEquals(expected, property.compareWith(() -> value).get() >= 0);
	}
	
	private static void assertLessThanOrEqualTo(IntProperty property, int value, boolean expected) {
		assertEquals(expected, property.isLessThanOrEqualTo(value));
		assertEquals(expected, property.isLessThanOrEqualToProperty(value).get());
		assertEquals(expected, property.isLessThanOrEqualToProperty(() -> value).get());
		assertEquals(expected, property.compareWith(value).get() <= 0);
		assertEquals(expected, property.compareWith(() -> value).get() <= 0);
	}
	
	MutableIntProperty mockMutableIntProperty() {
		return new SimpleMutableIntProperty();
	}
	
}
