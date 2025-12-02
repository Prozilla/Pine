package dev.prozilla.pine.core;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.shape.QuadPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.*;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.test.TestPerformanceExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestPerformanceExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
	
	private static final int FRAMES = 60_000;
	private static final float FPS = 60;
	
	// Smoke test
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
		
		world.addEntity(new QuadPrefab(Vector2f.one()));
		
		for (int i = 0; i < FRAMES; i++) {
			float deltaTime = 1f / FPS;
			application.input(deltaTime);
			application.update(deltaTime);
			application.render(application.getRenderer());
		}
		
		application.destroy();
	}
	
	@Test
	void testContextId() {
		Application application = mockApplication();
		application.addScene(new Scene());
		int contextId1 = application.getContextId();
		application.nextScene();
		int contextId2 = application.getContextId();
		assertTrue(contextId2 > contextId1, "contextId should increase when next scene is loaded");
		application.unloadScene();
		int contextId3 = application.getContextId();
		assertEquals(contextId2 + 1, contextId3, "contextId should increase by 1 when scene is unloaded");
	}
	
	Application mockApplication() {
		Application application = new Application("Application", 900, 600, new Scene(), 60, ApplicationMode.HEADLESS);
		application.getConfig().logging.errorHandler.setValue(Assertions::fail);
		return application;
	}
	
}
