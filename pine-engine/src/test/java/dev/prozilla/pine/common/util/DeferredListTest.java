package dev.prozilla.pine.common.util;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeferredListTest {
	
	@Test
	void testModificationDuringIteration() {
		DeferredList<String> deferredList = mockDeferredList();
		
		boolean found = false;
		for (String string : deferredList) {
			if (string.equals("two") && !found) {
				deferredList.remove(string);
				found = true;
			}
		}
	}
	
	@Test
	void testIsIterating() {
		DeferredList<String> deferredList = mockDeferredList();
		assertFalse(deferredList.isIterating());
		
		int iterationCount = 0;
		for (String string : deferredList) {
			assertTrue(deferredList.contains(string));
			
			iterationCount++;
			deferredList.remove(string);
			
			assertTrue(deferredList.isIterating());
			assertFalse(deferredList.getSnapshot().contains(string));
			assertEquals(deferredList.size() - iterationCount, deferredList.snapshotSize());
		}
		
		assertFalse(deferredList.isIterating());
		assertTrue(deferredList.isEmpty());
	}
	
	DeferredList<String> mockDeferredList() {
		DeferredList<String> deferredList = new DeferredList<>();
		deferredList.add("one");
		deferredList.add("two");
		deferredList.add("three");
		return deferredList;
	}
	
}
