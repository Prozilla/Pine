package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MutableObjectPropertyTest {
	
	@Test
	void testMutations() {
		MutableObjectProperty<Boolean> booleanProperty = mockProperty(true);
		assertTrue(booleanProperty.getValue());
		
		booleanProperty.setValue(false);
		assertFalse(booleanProperty.getValue());
		
		booleanProperty.setValue(true);
		assertTrue(booleanProperty.getValue());
		
		assertTrue(booleanProperty.swapValue(false));
		assertFalse(booleanProperty.getValue());
	}
	
	@Test
	void testView() {
		MutableObjectProperty<Boolean> booleanProperty = mockProperty(true);
		Property<Boolean> view = booleanProperty.viewProperty();
		assertTrue(view.getValue(), "view of boolean property should inherit value");
		if (view instanceof MutableBooleanProperty mutableView) {
			mutableView.set(false);
		}
		assertTrue(booleanProperty.getValue(), "boolean property should not be mutable via view");
		assertTrue(view.getValue(), "view of boolean property should not be mutable");
		booleanProperty.setValue(false);
		assertFalse(view.getValue(), "view of boolean property should inherit value");
	}
	
	MutableObjectProperty<Boolean> mockProperty(boolean initialValue) {
		return new SimpleMutableObjectProperty<>(initialValue);
	}
	
}
