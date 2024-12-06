package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.init.InitSystemBase;

import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * Initializes cameras by displaying their background color and settings their size based on the window's dimensions.
 */
public class CameraInitializer extends InitSystemBase {
	
	public CameraInitializer() {
		super(CameraData.class);
	}
	
	@Override
	public void init() {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(match -> {
			CameraData cameraData = match.getComponent(CameraData.class);
			
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
