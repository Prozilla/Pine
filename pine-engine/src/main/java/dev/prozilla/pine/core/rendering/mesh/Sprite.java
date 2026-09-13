package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.rendering.mesh.modifier.UVModifier;

public class Sprite extends Rect {
	
	private final UVModifier uvModifier;
	private final Vector2f textureSize;
	
	public Sprite(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public Sprite(TextureAsset texture) {
		this(texture, new Vector3f());
	}
	
	public Sprite(String texturePath, Vector3f position) {
		this(AssetPools.textures.load(texturePath), position);
	}
	
	public Sprite(TextureAsset texture, Vector3f position) {
		this(new Vector2f(texture.getSize()), position, new Vector2f(), new Vector2f(texture.getSize()));
	}
	
	public Sprite(Vector2f size, Vector3f position, Vector2f regionOffset, Vector2f regionSize) {
		this(size, position);
		setRegion(regionOffset, regionSize, size);
	}
	
	public Sprite(Vector2f size, Vector3f position) {
		super(position, size);
		this.textureSize = new Vector2f();
		uvModifier = new UVModifier();
		addModifier(uvModifier);
	}
	
	public Vector2f getOffset() {
		return origin.shrink();
	}
	
	public void setOffset(Vector2f offset) {
		setOffset(offset.x, offset.y);
	}
	
	public void setOffset(float x, float y) {
		setOriginX(x);
		setOriginY(y);
	}
	
	public void setRegion(Vector2f regionOffset, Vector2f regionSize, Vector2f textureSize) {
		this.textureSize.set(textureSize);
		setRegion(regionOffset, regionSize);
	}
	
	public void setRegion(float regionX, float regionY, float regionWidth, float regionHeight) {
		setRegion(new Vector2f(regionX, regionY), new Vector2f(regionWidth, regionHeight));
	}
	
	public void setRegion(Vector2f regionOffset, Vector2f regionSize) {
		uvModifier.setRegion(regionOffset, regionSize, textureSize);
		setSize(regionSize);
	}
	
	public void unsetRegion() {
		uvModifier.resetRegion();
		setSize(textureSize);
	}
	
	public void setFlipHorizontal(boolean flipHorizontal) {
		uvModifier.setFlipHorizontal(flipHorizontal);
	}
	
	public void setFlipVertical(boolean flipVertical) {
		uvModifier.setFlipVertical(flipVertical);
	}
	
	public UVModifier getUVModifier() {
		return uvModifier;
	}
}
