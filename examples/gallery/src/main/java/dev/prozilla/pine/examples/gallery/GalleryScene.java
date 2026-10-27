package dev.prozilla.pine.examples.gallery;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.property.vector.delegated.DelegatedVector3fProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.camera.Controllable3DCameraPrefab;
import dev.prozilla.pine.core.entity.prefab.light.SunLightPrefab;
import dev.prozilla.pine.core.entity.prefab.mesh.MeshPrefab;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class GalleryScene extends Scene {
	
	@Override
	protected void load() {
		super.load(new Controllable3DCameraPrefab(10f, 0.05f));
		
		cameraData.setSkybox("skybox/right.jpg");
		cameraData.fieldOfView = 70;
		
		String modelPath = getModelPath();
		if (modelPath != null) {
			Prefab parentPrefab = new Prefab();
			for (MeshPrefab<StaticMesh> meshPrefab : AssetPools.models.load(getModelPath()).createPrefabs()) {
				meshPrefab.setScale(new Vector3f(getModelScale()));
				parentPrefab.addChild(meshPrefab);
			}
			
			spawnObject(parentPrefab, new Vector3f(), new Vector3f());
		}
		
		SunLightPrefab sunLightPrefab = new SunLightPrefab(Color.white(), 1f);
		sunLightPrefab.setRotation(new DelegatedVector3fProperty(new FixedFloatProperty(35), () -> getTimer().getScaledTime() * 18f, FixedFloatProperty.ZERO));
		addEntity(sunLightPrefab);
		
		getInput().disableCursor();
	}
	
	private void spawnObject(Prefab prefab, Vector3f position, Vector3f rotation) {
		prefab.setPosition(position);
		prefab.setRotation(rotation);
		
		addEntity(prefab);
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.ESCAPE)) {
			application.stop();
		} else if (input.getKeyDown(Key.P) || input.getKeyDown(Key.SPACE)) {
			application.togglePause();
		} else if (input.getKeyDown(Key.F5)) {
			application.reloadScene();
		}
	}
	
	protected String getModelPath() {
		return null;
	}
	
	protected float getModelScale() {
		return 1;
	}
	
}
