package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.selection.WrapMode;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SelectionPropertyTest {
	
	@Test
	void testWrapModeRepeat() {
		WrapMode wrapMode = WrapMode.REPEAT;
		
		assertEquals(4, wrapMode.transform(15, 0, 10));
		assertEquals(0, wrapMode.transform(0, 0, 10));
		assertEquals(10, wrapMode.transform(10, 0, 10));
		assertEquals(1, wrapMode.transform(11, 1, 10));
		assertEquals(10, wrapMode.transform(0, 1, 10));
	}
	
	@Test
	void testWrapModeClip() {
		WrapMode wrapMode = WrapMode.CLIP;
		
		assertEquals(-1, wrapMode.transform(15, 0, 10));
		assertEquals(0, wrapMode.transform(0, 0, 10));
		assertEquals(10, wrapMode.transform(10, 0, 10));
		assertEquals(-1, wrapMode.transform(11, 1, 10));
	}
	
	@Test
	void testWrapModeClamp() {
		WrapMode wrapMode = WrapMode.CLAMP;
		
		assertEquals(2, wrapMode.transform(2, 0, 10));
		assertEquals(10, wrapMode.transform(15, 0, 10));
		assertEquals(0, wrapMode.transform(0, 0, 10));
		assertEquals(10, wrapMode.transform(10, 0, 10));
		assertEquals(10, wrapMode.transform(11, 1, 10));
	}
	
	@Test
	void testWrapModesWithinBounds() {
		int min = 0;
		int max = 10;
		for (WrapMode wrapMode : WrapMode.values()) {
			for (int i = min; i <= max; i++) {
				int wrapped = wrapMode.transform(i, min, max);
				assertEquals(i, wrapped);
			}
		}
	}
	
}
