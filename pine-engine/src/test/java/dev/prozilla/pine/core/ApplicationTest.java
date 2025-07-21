package dev.prozilla.pine.core;

import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
	
	@Test
	void testLifecycle() {
		Application application = mockApplication();
		application.init();
		assertTrue(application.isRunning());
		
		application.input(0);
		application.update(0);
		application.render(application.getRenderer());
		application.destroy();
		assertFalse(application.isRunning());
	}
	
	Application mockApplication() {
		Application application = new Application("Application", 900, 600, new Scene(), 60, ApplicationMode.HEADLESS);
		application.getConfig().logging.errorHandler.setValue(Assertions::fail);
		return application;
	}
	
}
