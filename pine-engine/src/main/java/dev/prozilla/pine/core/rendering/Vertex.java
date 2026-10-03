package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;

public class Vertex {

	public Vector3f position;
	public Color color;
	public Vector2f textureCoordinates;
	public float textureId;
	public float textureType;
	
	public Vertex() {
		position = new Vector3f();
		color = new Color();
		textureCoordinates = new Vector2f();
	}
	
	public Vertex setPosition(Vector3f position) {
		this.position.receive(position);
		return this;
	}
	
	public Vertex setColor(Color color) {
		this.color.receive(color);
		return this;
	}
	
	public Vertex setTextureCoordinates(Vector2f textureCoordinates) {
		this.textureCoordinates.receive(textureCoordinates);
		return this;
	}

	public Vertex resetTexture() {
		return setTexture(null);
	}
	
	public Vertex setTexture(TextureAsset texture) {
		if (texture != null) {
			textureId = texture.getId();
			textureType = texture.isInArray() ? 1f : 0f;
		} else {
			textureId = -1;
			textureType = 0f;
		}
		return this;
	}
	
}
