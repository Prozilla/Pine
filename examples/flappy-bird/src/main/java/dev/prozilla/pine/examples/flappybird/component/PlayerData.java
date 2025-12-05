package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.deserialized.FileDeserializer;
import dev.prozilla.pine.common.property.input.*;
import dev.prozilla.pine.common.property.observable.ObservableFloatProperty;
import dev.prozilla.pine.core.component.deserialization.DeserializedData;
import dev.prozilla.pine.core.component.physics.collision.CircleCollider;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PlayerData extends DeserializedData<PlayerData.Data> {
	
	public int animationFrame;
	public float age;
	public float velocity;
	
	public ObservableFloatProperty jumpVelocity;
	public ObservableFloatProperty speed;
	public ObservableFloatProperty animationSpeed;
	public ObservableFloatProperty rotationFactor;
	public ObservableFloatProperty rotationSpeed;
	
	public final InputBindings jumpButton = new InputBindings(
		new KeyboardKeyProperty(Key.SPACE),
		new MouseButtonProperty(MouseButton.LEFT),
		new GamepadButtonProperty(GamepadButton.A)
	);
	public final InputBinding pauseButton = new InputBinding(Key.ESCAPE);
	
	public GameScene gameScene;
	public final CircleCollider collider;
	
	// Constants
	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 32;
	public static final float SCALE = 1.5f;
	public static final float WIDTH = SPRITE_WIDTH * SCALE;
	public static final float HEIGHT = SPRITE_HEIGHT * SCALE;
	public static final float POSITION_X = FlappyBird.WIDTH / -4f;
	public static final float COLLIDER_RADIUS = 14 * SCALE;
	public static final Vector2f COLLIDER_OFFSET = new Vector2f(16, 20).scale(SCALE);
	
	public static class Data {
		
		public float jumpVelocity = 0.65f;
		public float speed = 650f;
		public float animationSpeed = 10f;
		public float rotationFactor = 50f;
		public float rotationSpeed = 10f;
		
	}
	
	public PlayerData(CircleCollider collider) {
		this.collider = collider;
	}
	
	@Override
	public void readData(FileDeserializer<Data> deserializer) {
		super.readData(deserializer);
		
		deserializer.setAlwaysCreateData(true);
		jumpVelocity = deserializer.createFloatProperty((data) -> data.jumpVelocity);
		speed = deserializer.createFloatProperty((data) -> data.speed);
		animationSpeed = deserializer.createFloatProperty((data) -> data.animationSpeed);
		rotationFactor = deserializer.createFloatProperty((data) -> data.rotationFactor);
		rotationSpeed = deserializer.createFloatProperty((data) -> data.rotationSpeed);
	}
	
	public void resetVelocity() {
		if (velocity > 0) {
			velocity = 0;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (deserializer != null) {
			deserializer.destroy();
		}
	}
	
}
