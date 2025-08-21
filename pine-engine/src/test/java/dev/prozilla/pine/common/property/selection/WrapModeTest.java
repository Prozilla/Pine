package dev.prozilla.pine.common.property.selection;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WrapModeTest {
	
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
	
	@Test
	void testListResizingRepeat() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		WrapMode.REPEAT.resizeList(list, 5);
		assertEquals(5, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(1, list.get(3));
		assertEquals(2, list.get(4));
	}
	
	@Test
	void testListResizingClip() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		WrapMode.CLIP.resizeList(list, 5);
		assertEquals(5, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertNull(list.get(3));
		assertNull(list.get(4));
	}
	
	@Test
	void testListResizingClamp() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		WrapMode.CLAMP.resizeList(list, 5);
		assertEquals(5, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(3, list.get(3));
		assertEquals(3, list.get(4));
	}
	
}
