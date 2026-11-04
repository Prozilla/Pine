package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.Holdable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Material<S extends ShaderProgram> implements Holdable {

	protected final S shaderProgram;
	
	protected TextureAsset texture;
	public Color color;
	public boolean snapPixels;
	
	private int holderCount;
	
	public Material(S shaderProgram) {
		this(shaderProgram, null, null);
	}
	
	public Material(S shaderProgram, Color color) {
		this(shaderProgram, null, color);
	}
	
	public Material(S shaderProgram, TextureAsset texture) {
		this(shaderProgram, texture, null);
	}
	
	public Material(S shaderProgram, TextureAsset texture, Color color) {
		this.shaderProgram = Checks.isNotNull(shaderProgram, "shaderProgram");
		this.texture = Holdable.hold(texture);
		this.color = color;
		snapPixels = false;
	}
	
	public S getShaderProgram() {
		return shaderProgram;
	}
	
	public boolean hasTexture() {
		return texture != null;
	}
	
	public TextureAsset getTexture() {
		return texture;
	}
	
	public void setTexture(String texturePath) {
		setTexture(AssetPools.textures.load(texturePath));
	}
	
	public void setTexture(TextureAsset texture) {
		this.texture = Holdable.swap(this.texture, texture);
	}
	
	public boolean hasColor() {
		return color != null;
	}
	
	public void bind() {
		shaderProgram.snapPixelsUniform.set(snapPixels);
	}
	
	@Override
	public void hold() {
		holderCount++;
	}
	
	@Override
	public void drop() {
		holderCount--;
		checkHoldState();
	}
	
	@Override
	public int getHolderCount() {
		return holderCount;
	}
	
	@Override
	public void destroy() {
		texture = Holdable.drop(texture);
	}
	
	@Override
	public String toString() {
		return Printable.objectToString(this,
			"shaderProgram", shaderProgram,
			"texture", texture,
			"color", color);
	}
	
}
