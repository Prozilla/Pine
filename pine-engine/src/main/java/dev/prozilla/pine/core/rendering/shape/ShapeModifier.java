package dev.prozilla.pine.core.rendering.shape;

public interface ShapeModifier {
	
	float[] modifyVertices(float[] vertices);
	
	float[] modifyUVs(float[] oldVertices, float[] newVertices, float[] uvArray);

}
