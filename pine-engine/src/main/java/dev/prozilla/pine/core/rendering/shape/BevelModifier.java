package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class BevelModifier implements ShapeModifier {
	
	private float bevelAmount;
	private int segments;
	
	public BevelModifier(float bevelAmount, int segments) {
		this.bevelAmount = bevelAmount;
		this.segments = segments;
	}
	
	@Override
	public float[] modifyVertices(float[] vertices) {
		List<Float> newVertices = new ArrayList<>();
		
		for (int i = 0; i < vertices.length; i += 6) {
			Vector2f a = new Vector2f(vertices[i], vertices[i + 1]);
			Vector2f b = new Vector2f(vertices[i + 2], vertices[i + 3]);
			Vector2f c = new Vector2f(vertices[i + 4], vertices[i + 5]);
			
			// Bevel each corner of triangle a-b-c
			List<Vector2f> beveled = bevelTriangle(a, b, c);
			
			// Triangulate the beveled polygon (just connect center)
			Vector2f center = new Vector2f(
				(a.x + b.x + c.x) / 3f,
				(a.y + b.y + c.y) / 3f
			);
			
			for (int j = 0; j < beveled.size(); j++) {
				Vector2f p1 = beveled.get(j);
				Vector2f p2 = beveled.get((j + 1) % beveled.size());
				
				newVertices.add(center.x);
				newVertices.add(center.y);
				
				newVertices.add(p1.x);
				newVertices.add(p1.y);
				
				newVertices.add(p2.x);
				newVertices.add(p2.y);
			}
		}
		
		// Convert to float[]
		float[] out = new float[newVertices.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = newVertices.get(i);
		}
		return out;
	}
	
	private List<Vector2f> bevelTriangle(Vector2f a, Vector2f b, Vector2f c) {
		List<Vector2f> result = new ArrayList<>();
		
		bevelCorner(c, a, b, result); // corner A
		bevelCorner(a, b, c, result); // corner B
		bevelCorner(b, c, a, result); // corner C
		
		return result;
	}
	
	private void bevelCorner(Vector2f prev, Vector2f corner, Vector2f next, List<Vector2f> out) {
		Vector2f dir1 = new Vector2f(corner.x - prev.x, corner.y - prev.y);
		Vector2f dir2 = new Vector2f(corner.x - next.x, corner.y - next.y);
		
		normalize(dir1);
		normalize(dir2);
		
		Vector2f p1 = new Vector2f(corner.x - dir1.x * bevelAmount, corner.y - dir1.y * bevelAmount);
		Vector2f p2 = new Vector2f(corner.x - dir2.x * bevelAmount, corner.y - dir2.y * bevelAmount);
		
		// Optional: Subdivide arc if resolution > 1
		if (segments <= 1) {
			out.add(p1);
			out.add(p2);
		} else {
			float angle1 = (float) Math.atan2(-dir1.y, -dir1.x);
			float angle2 = (float) Math.atan2(-dir2.y, -dir2.x);
			if (angle2 < angle1) angle2 += Math.PI * 2;
			
			for (int i = 0; i <= segments; i++) {
				float t = (float) i / segments;
				float angle = angle1 + t * (angle2 - angle1);
				float x = (float) (corner.x + Math.cos(angle) * -bevelAmount);
				float y = (float) (corner.y + Math.sin(angle) * -bevelAmount);
				out.add(new Vector2f(x, y));
			}
		}
	}
	
	private void normalize(Vector2f v) {
		float len = (float) Math.sqrt(v.x * v.x + v.y * v.y);
		if (len != 0f) {
			v.x /= len;
			v.y /= len;
		}
	}
	
	@Override
	public float[] modifyUVs(float[] oldVertices, float[] newVertices, float[] uvArray) {
		List<Float> newUVs = new ArrayList<>();
		
		for (int i = 0; i < newVertices.length; i += 2) {
			// Naive UV mapping: map based on bounding box
			float x = newVertices[i];
			float y = newVertices[i + 1];
			
			float u = x; // These should be remapped based on shape bounds
			float v = y;
			
			newUVs.add(u);
			newUVs.add(v);
		}
		
		// Normalize to 0..1
		normalizeUVs(newUVs);
		
		// Convert to float[]
		float[] newUVArray = new float[newUVs.size()];
		for (int i = 0; i < newUVArray.length; i++) {
			newUVArray[i] = newUVs.get(i);
		}
		return newUVArray;
	}
	
	private void normalizeUVs(List<Float> uvs) {
		float minU = Float.MAX_VALUE, maxU = -Float.MAX_VALUE;
		float minV = Float.MAX_VALUE, maxV = -Float.MAX_VALUE;
		
		for (int i = 0; i < uvs.size(); i += 2) {
			float u = uvs.get(i);
			float v = uvs.get(i + 1);
			if (u < minU) minU = u;
			if (u > maxU) maxU = u;
			if (v < minV) minV = v;
			if (v > maxV) maxV = v;
		}
		
		float scaleU = maxU - minU;
		float scaleV = maxV - minV;
		
		for (int i = 0; i < uvs.size(); i += 2) {
			float u = uvs.get(i);
			float v = uvs.get(i + 1);
			uvs.set(i, (u - minU) / scaleU);
			uvs.set(i + 1, (v - minV) / scaleV);
		}
	}
	
	public void setBevelAmount(float bevelAmount) {
		this.bevelAmount = bevelAmount;
	}
	
	public void setSegments(int segments) {
		this.segments = segments;
	}
	
}
