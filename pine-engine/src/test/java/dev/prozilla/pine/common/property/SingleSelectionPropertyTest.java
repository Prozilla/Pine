package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.selection.SingleSelectionProperty;
import dev.prozilla.pine.common.property.selection.WrapMode;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleSelectionPropertyTest {
	
	@Test
	void testSelectionWithWrapModeRepeat() {
		SingleSelectionProperty<Integer> selectionProperty = mockSingleSelectionProperty();
		selectionProperty.setWrapMode(WrapMode.REPEAT);
		
		assertFalse(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		
		selectionProperty.selectItem(5);
		
		assertTrue(selectionProperty.isAnyItemSelected());
		assertTrue(selectionProperty.isItemSelected(5));
		assertFalse(selectionProperty.isItemSelected(10));
		
		selectionProperty.selectNext();
		
		assertTrue(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		assertTrue(selectionProperty.isItemSelected(10));
		
		selectionProperty.selectPrevious();
		selectionProperty.selectPrevious();
		
		assertTrue(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		assertFalse(selectionProperty.isItemSelected(10));
		assertTrue(selectionProperty.isItemSelected(20));
	}
	
	@Test
	void testSelectionWithWrapModeClamp() {
		SingleSelectionProperty<Integer> selectionProperty = mockSingleSelectionProperty();
		selectionProperty.setWrapMode(WrapMode.CLAMP);
		
		assertFalse(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		
		selectionProperty.selectItem(5);
		selectionProperty.selectPrevious();
		selectionProperty.selectPrevious();
		
		assertTrue(selectionProperty.isAnyItemSelected());
		assertTrue(selectionProperty.isItemSelected(5));
		
		selectionProperty.selectItem(20);
		selectionProperty.selectNext();
		selectionProperty.selectNext();
		
		assertTrue(selectionProperty.isAnyItemSelected());
		assertTrue(selectionProperty.isItemSelected(20));
	}
	
	@Test
	void testSelectionWithWrapModeClip() {
		SingleSelectionProperty<Integer> selectionProperty = mockSingleSelectionProperty();
		selectionProperty.setWrapMode(WrapMode.CLIP);
		
		assertFalse(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		
		selectionProperty.selectItem(5);
		selectionProperty.selectPrevious();
		selectionProperty.selectPrevious();
		
		assertFalse(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(5));
		
		selectionProperty.selectItem(20);
		selectionProperty.selectNext();
		selectionProperty.selectNext();
		
		assertFalse(selectionProperty.isAnyItemSelected());
		assertFalse(selectionProperty.isItemSelected(20));
	}
	
	SingleSelectionProperty<Integer> mockSingleSelectionProperty() {
		SingleSelectionProperty<Integer> singleSelectionProperty = new SingleSelectionProperty<>();
		singleSelectionProperty.addItem(5);
		singleSelectionProperty.addItem(10);
		singleSelectionProperty.addItem(15);
		singleSelectionProperty.addItem(20);
		return singleSelectionProperty;
	}
	
}
