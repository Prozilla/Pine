package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.common.property.deserialized.FileDeserializer;
import dev.prozilla.pine.common.property.observable.ObservableProperty;
import dev.prozilla.pine.core.component.deserialization.DeserializedData;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PlayerData extends DeserializedData<PlayerData.Data> {
	
	public int animationFrame;
	public float age;
	public float velocity;
	
	public ObservableProperty<Float> jumpVelocity;
	public ObservableProperty<Float> speed;
	public ObservableProperty<Float> animationSpeed;
	public ObservableProperty<Float> rotationFactor;
	public ObservableProperty<Float> rotationSpeed;
	
	public GameScene gameScene;
	
	// Constants
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final float POSITION_X = FlappyBird.WIDTH / -4f;
	
	public static class Data {
		
		public Float jumpVelocity;
		public Float speed;
		public Float animationSpeed;
		public Float rotationFactor;
		public Float rotationSpeed;
		
	}
	
	@Override
	public void readData(FileDeserializer<Data> deserializer) {
		super.readData(deserializer);
		
		jumpVelocity = deserializer.createProperty((data) -> data.jumpVelocity, 0.65f);
		speed = deserializer.createProperty((data) -> data.speed, 650f);
		animationSpeed = deserializer.createProperty((data) -> data.animationSpeed, 10f);
		rotationFactor = deserializer.createProperty((data) -> data.rotationFactor, 50f);
		rotationSpeed = deserializer.createProperty((data) -> data.rotationSpeed, 10f);
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
