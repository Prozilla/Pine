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
	
	@Test
	void testVersion() {
		assertNotNull(Pine.getVersion());
		assertFalse(Pine.getVersion().isBlank());
	}
	
	@Test
	void testPlatformName() {
		assertNotNull(Pine.getPlatformName());
		assertFalse(Pine.getVersion().isBlank());
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
		assertNotNull(Pine.getJavaVersion());
		assertFalse(Pine.getJavaVersion().isBlank());
	}
	
	@Test
	void testLWJGLVersion() {
		assertNotNull(Pine.getLWJGLVersion());
		assertFalse(Pine.getLWJGLVersion().isBlank());
	}
	
	@Test
	void testLGLFWVersion() {
		assertNotNull(Pine.getGLFWVersion());
		assertFalse(Pine.getGLFWVersion().isBlank());
	}
	
}
