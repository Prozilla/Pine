package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.InitSystem;

import static org.lwjgl.opengl.GL11.glClearColor;

public class CameraInitializer extends InitSystem {
	
	public CameraInitializer() {
		super(new ComponentCollector(CameraData.class));
	}
	
	@Override
	public void init(long window) {
		int width = world.application.getWindow().getWidth();
		int height = world.application.getWindow().getHeight();
		
		forEach(componentGroup -> {
			CameraData cameraData = componentGroup.getComponent(CameraData.class);
			
			renderBackgroundColor(cameraData.backgroundColor);
			cameraData.setSize(width, height);
		});
	}
	
	private void renderBackgroundColor(Color color) {
		if (!Application.initializedOpenGL) {
			throw new RuntimeException("Can't render background color before initialization");
		}
		
		glClearColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
	}
}
