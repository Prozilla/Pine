package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.InputSystem;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.entity.Player;

public class PlayerInputHandler extends InputSystem {
	
	public PlayerInputHandler() {
		super(Player.collector);
	}
	
	@Override
	public void input(float deltaTime) {
		Input input = getInput();
		
		forEach(componentGroup -> {
			PlayerData playerData = componentGroup.getComponent(PlayerData.class);
			
			if (!playerData.gameScene.gameOver) {
				// Jump
				if (input.getKeyDown(Key.SPACE) || input.getMouseButtonDown(MouseButton.LEFT)) {
					playerData.velocity = PlayerData.JUMP_VELOCITY;
				}
			}
		});
	}
}
