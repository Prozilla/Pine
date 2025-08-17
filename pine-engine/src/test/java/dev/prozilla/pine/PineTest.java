package dev.prozilla.pine;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PineTest {
	
	private static final boolean ENABLE_LOGGING = true;
	
	@Test
	void testVersion() {
		testPineString(Pine.getVersion(), "Pine Version");
	}
	
	@Test
	void testPlatformName() {
		testPineString(Pine.getPlatformName(), "Platform Name");
	}
	
	@Test
	void testPlatform() {
		assertNotNull(Pine.getPlatform());
	}
	
	@Test
	void testArchitecture() {
		assertNotNull(Pine.getArchitecture());
	}
	
	@Test
	void testJavaVersion() {
		testPineString(Pine.getJavaVersion(), "Java Version");
	}
	
	@Test
	void testLWJGLVersion() {
		testPineString(Pine.getLWJGLVersion(), "LWJGL Version");
	}
	
	@Test
	void testLGLFWVersion() {
		testPineString(Pine.getGLFWVersion(), "GLFW Version");
	}
	
	void testPineString(String string, String label) {
		assertNotNull(string);
		assertFalse(string.isBlank());
		if (ENABLE_LOGGING) {
			TestLoggingExtension.log(label + ": " + string);
		}
	}
	
}
