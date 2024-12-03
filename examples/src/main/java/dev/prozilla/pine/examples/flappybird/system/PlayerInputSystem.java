package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.system.InputSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PlayerInputSystem extends InputSystem{
	
	public PlayerInputSystem() {
		super(new ComponentCollector(PlayerData.class));
	}
	
	@Override
	public void input(float deltaTime) {
	
	}
}
