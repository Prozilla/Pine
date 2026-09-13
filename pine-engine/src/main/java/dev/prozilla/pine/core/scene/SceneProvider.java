package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.ProviderOf;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.camera.OverlayCameraData;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.entity.EntityQueryPool;
import dev.prozilla.pine.core.system.SystemManager;

@ProviderOf(Scene.class)
@FunctionalInterface
public interface SceneProvider extends SceneContext {
	
	Scene getScene();
	
	@Override
	default EntityManager getEntityManager() {
		return getScene().getEntityManager();
	}
	
	@Override
	default ComponentManager getComponentManager() {
		return getScene().getComponentManager();
	}
	
	@Override
	default SystemManager getSystemManager() {
		return getScene().getSystemManager();
	}
	
	@Override
	default EntityQueryPool getQueryPool() {
		return getScene().getQueryPool();
	}
	
	@Override
	default CameraData getCameraData() {
		return getScene().getCameraData();
	}
	
	@Override
	default OverlayCameraData getOverlayCameraData() {
		return getScene().getOverlayCameraData();
	}
}
