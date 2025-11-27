package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MutableIntPropertyTest {
	
	@Test
	void testMutations() {
		MutableIntProperty intProperty = mockProperty(1);
		assertEquals(1, intProperty.get());
		
		intProperty.set(0);
		assertEquals(0, intProperty.get());
		
		intProperty.set(1);
		assertEquals(1, intProperty.get());
		
		assertEquals(1, intProperty.swap(0));
		assertEquals(0, intProperty.get());
	}
	
	@Test
	void testView() {
		MutableIntProperty intProperty = mockProperty(1);
		IntProperty view = intProperty.viewProperty();
		assertEquals(1, view.get(), "view of int property should inherit value");
		if (view instanceof MutableIntProperty mutableView) {
			mutableView.set(0);
		}
		assertEquals(1, intProperty.get(), "int property should not be mutable via view");
		assertEquals(1, (int)view.getValue(), "view of int property should not be mutable");
		intProperty.set(0);
		assertEquals(0, view.get(), "view of int property should inherit value");
	}
	
	MutableIntProperty mockProperty(int initialValue) {
		return new SimpleMutableIntProperty(initialValue);
	}
	
}
