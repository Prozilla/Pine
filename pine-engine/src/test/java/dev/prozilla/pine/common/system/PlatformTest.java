package dev.prozilla.pine.common.system;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static dev.prozilla.pine.test.TestUtils.*;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlatformTest {
	
	private static final boolean ENABLE_LOGGING = true;
	
	@Test
	void testPersistentDataPath() {
		String persistentDataPath = Platform.getPersistentDataPath("test");
		assertNonBlankString(persistentDataPath);
		assertEndsWith(persistentDataPath, "/test/");
		if (ENABLE_LOGGING) {
			TestLoggingExtension.log("Persistent data path: " + persistentDataPath);
		}
	}
	
	@Test
	void testName() {
		for (Platform platform : Platform.values()) {
			assertNonBlankString(platform.getName());
		}
	}
	
	@Test
	void testIdentifier() {
		for (Platform platform : Platform.values()) {
			assertNonBlankString(platform.getIdentifier());
		}
	}
	
	@Test
	void testUserPath() {
		String userHome = "/home/";
		String separator = "/";
		for (Platform platform : Platform.values()) {
			assertNonBlankString(platform.getUserPath(userHome, separator));
		}
	}
	
	@Test
	void testDescriptor() {
		String descriptor = Platform.getDescriptor();
		assertNonBlankString(descriptor);
		assertContainsOnce(descriptor, "/");
		if (ENABLE_LOGGING) {
			TestLoggingExtension.log("Platform descriptor: " + descriptor);
		}
	}
	
}
