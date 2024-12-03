package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.system.InputSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;

public class PlayerInputSystem extends InputSystem<PlayerData> {
	
	public PlayerInputSystem() {
		super(PlayerData.class);
	}
	
	@Override
	public void input(float deltaTime) {
	
	}
}
