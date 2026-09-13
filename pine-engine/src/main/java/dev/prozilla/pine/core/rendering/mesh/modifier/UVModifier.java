package dev.prozilla.pine.core.rendering.mesh.modifier;

import dev.prozilla.pine.common.math.vector.Vector2f;

// TODO: Replace with transformation matrix to support more transformations
public class UVModifier extends MeshModifier {
	
	private float uMin;
	private float vMin;
	private float uMax;
	private float vMax;
	private boolean flipHorizontal;
	private boolean flipVertical;
	
	public UVModifier() {
		this(0f, 0f, 1f, 1f);
	}
	
	public UVModifier(float uMin, float vMin, float uMax, float vMax) {
		this.uMin = uMin;
		this.vMin = vMin;
		this.uMax = uMax;
		this.vMax = vMax;
	}
	
	@Override
	public ModifiedMesh apply(float[] vertices, float[] uvArray, int[] triangles) {
		int uvCount = uvArray.length / 2;
		float[] newUvArray = new float[uvArray.length];
		
		float uRange = uMax - uMin;
		float vRange = vMax - vMin;
		
		for (int i = 0; i < uvCount; i++) {
			float u = uvArray[i * 2];
			float v = uvArray[i * 2 + 1];
			
			float newU = uMin + u * uRange;
			float newV = vMin + v * vRange;
			
			if (flipHorizontal) {
				newU = uMin + (1f - u) * uRange;
			}
			if (flipVertical) {
				newV = vMin + (1f - v) * vRange;
			}
			
			newUvArray[i * 2] = newU;
			newUvArray[i * 2 + 1] = newV;
		}
		
		return new ModifiedMesh(vertices, newUvArray, triangles);
	}
	
	public void resetRegion() {
		setRegion(0, 0, 1, 1);
	}
	
	public void setRegion(Vector2f offset, Vector2f size, Vector2f textureSize) {
		setRegion(offset.x / textureSize.x, offset.y / textureSize.y, (offset.x + size.x) / textureSize.x, (offset.y + size.y) / textureSize.y);
	}
	
	public void setRegion(float uMin, float vMin, float uMax, float vMax) {
		if (this.uMin == uMin && this.vMin == vMin && this.uMax == uMax && this.vMax == vMax) {
			return;
		}
		
		this.uMin = uMin;
		this.vMin = vMin;
		this.uMax = uMax;
		this.vMax = vMax;
		markAsDirty();
	}
	
	public void setFlipHorizontal(boolean flipHorizontal) {
		if (this.flipHorizontal == flipHorizontal) {
			return;
		}
		
		this.flipHorizontal = flipHorizontal;
		markAsDirty();
	}
	
	public void setFlipVertical(boolean flipVertical) {
		if (this.flipVertical == flipVertical) {
			return;
		}
		
		this.flipVertical = flipVertical;
		markAsDirty();
	}
}
