package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.ContextOf;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.entity.EntityQueryPool;
import dev.prozilla.pine.core.system.SystemManager;

@ContextOf(Scene.class)
public interface SceneContext {
	
	EntityManager getEntityManager();
	
	ComponentManager getComponentManager();
	
	SystemManager getSystemManager();
	
	EntityQueryPool getQueryPool();
	
	CameraData getCameraData();
	
}
