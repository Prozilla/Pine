package dev.prozilla.pine.core;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.shape.RectPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.*;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.test.TestPerformanceExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestPerformanceExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
	
	private static final int FRAMES = 60_000;
	private static final float FPS = 60;
	
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
	
	@Test
	@Tag("performance")
	void testPerformance() {
		Application application = mockApplication();
		application.init();
		
		World world = application.getCurrentScene().getWorld();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		nodeRoot.addChild(new LayoutPrefab());
		nodeRoot.addChild(new TextPrefab("Test"));
		nodeRoot.addChild(new TextInputPrefab());
		nodeRoot.addChild(new TextButtonPrefab("Test"));
		
		world.addEntity(new RectPrefab(Vector2f.one()));
		
		for (int i = 0; i < FRAMES; i++) {
			float deltaTime = 1f / FPS;
			application.input(deltaTime);
			application.update(deltaTime);
			application.render(application.getRenderer());
		}
		
		application.destroy();
	}
	
	Application mockApplication() {
		Application application = new Application("Application", 900, 600, new Scene(), 60, ApplicationMode.HEADLESS);
		application.getConfig().logging.errorHandler.setValue(Assertions::fail);
		return application;
	}
	
}
