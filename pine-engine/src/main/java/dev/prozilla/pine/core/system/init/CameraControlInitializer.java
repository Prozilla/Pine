package dev.prozilla.pine.core.system.init;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.KeyBindings;
import dev.prozilla.pine.core.system.InitSystem;

public class CameraControlInitializer extends InitSystem {
	
	public CameraControlInitializer() {
		super(new ComponentCollector(CameraControlData.class, Transform.class, CameraData.class));
	}
	
	@Override
	public void init(long window) {
		Input input = world.application.getInput();
		
		forEach(componentGroup -> {
			Transform transform = componentGroup.getComponent(Transform.class);
			CameraData cameraData = componentGroup.getComponent(CameraData.class);
			CameraControlData cameraControlData = componentGroup.getComponent(CameraControlData.class);
			
			KeyBindings<CameraControlData.Action> keyBindings = new KeyBindings<>(input);
			
			keyBindings.addAction(CameraControlData.Action.FORWARD, new Key[] { Key.UP_ARROW, Key.W });
			keyBindings.addAction(CameraControlData.Action.BACKWARDS, new Key[] { Key.DOWN_ARROW, Key.S });
			keyBindings.addAction(CameraControlData.Action.RIGHT, new Key[] { Key.RIGHT_ARROW, Key.D });
			keyBindings.addAction(CameraControlData.Action.LEFT, new Key[] { Key.LEFT_ARROW, Key.A });
			keyBindings.addAction(CameraControlData.Action.ZOOM_IN, Key.X);
			keyBindings.addAction(CameraControlData.Action.ZOOM_OUT, Key.Z);
			keyBindings.addAction(CameraControlData.Action.SPEED_UP, Key.L_SHIFT);
			keyBindings.addAction(CameraControlData.Action.SLOW_DOWN, Key.L_CONTROL);
			
			cameraControlData.keyBindings = keyBindings;
			
			cameraControlData.targetX = transform.x;
			cameraControlData.targetY = transform.y;
			cameraControlData.targetZoom = cameraData.zoomFactor;
		});
	}
}
