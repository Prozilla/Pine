package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;

public class Vertex {

	public final Vector3f position;
	public final Vector3f normal;
	public final Color color;
	public final Vector2f textureCoordinates;
	public float textureId;
	public float textureType;
	
	public Vertex() {
		position = new Vector3f();
		normal = new Vector3f();
		color = new Color();
		textureCoordinates = new Vector2f();
	}
	
	public Vertex setPosition(Vector3f position) {
		this.position.receive(position);
		return this;
	}
	
	public Vertex setNormal(Vector3f normal) {
		this.normal.receive(normal);
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
	
	public void reset() {
		position.set(0);
		normal.set(0);
		color.set(0, 0, 0, 1f);
		textureCoordinates.set(0);
		resetTexture();
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
