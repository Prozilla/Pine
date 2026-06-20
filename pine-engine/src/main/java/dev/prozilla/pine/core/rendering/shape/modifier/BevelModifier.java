package dev.prozilla.pine.core.rendering.shape.modifier;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BevelModifier extends ShapeModifier {
	
	private float bevelAmount;
	private int segments;
	
	public BevelModifier(float bevelAmount) {
		this(bevelAmount, 0);
		setAutoSegments();
	}
	
	public BevelModifier(float bevelAmount, int segments) {
		this.bevelAmount = bevelAmount;
		this.segments = segments;
	}
	
	@Override
	public float[] modifyVertices(float[] vertices) {
		if (segments < 1 || bevelAmount == 0) {
			return vertices;
		}
		
		// Reconstruct polygon
		List<Vector3f> polygon = extractPolygonFromTriangles(vertices);
		
		// Bevel each corner
		List<Vector3f> beveledPolygon = new ArrayList<>();
		int n = polygon.size();
		
		for (int i = 0; i < n; i++) {
			Vector3f prev = polygon.get((i - 1 + n) % n);
			Vector3f curr = polygon.get(i);
			Vector3f next = polygon.get((i + 1) % n);
			
			List<Vector3f> cornerPoints = bevelCorner(prev, curr, next);
			beveledPolygon.addAll(cornerPoints);
		}
		
		// Triangulate using center-fan approach
		Vector3f center = computeCentroid(beveledPolygon);
		List<Float> newVertices = new ArrayList<>();
		
		for (int i = 0; i < beveledPolygon.size(); i++) {
			Vector3f p1 = beveledPolygon.get(i);
			Vector3f p2 = beveledPolygon.get((i + 1) % beveledPolygon.size());
			
			newVertices.add(center.x);
			newVertices.add(center.y);
			newVertices.add(center.z);
			
			newVertices.add(p1.x);
			newVertices.add(p1.y);
			newVertices.add(p1.z);
			
			newVertices.add(p2.x);
			newVertices.add(p2.y);
			newVertices.add(p2.z);
		}
		
		float[] out = new float[newVertices.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = newVertices.get(i);
		}
		return out;
	}
	
	private Vector3f computeCentroid(List<Vector3f> points) {
		Vector3f centroid = new Vector3f();
		for (Vector3f point : points) {
			centroid.x += point.x;
			centroid.y += point.y;
			centroid.z += point.z;
		}
		return centroid.divide(points.size());
	}
	
	private List<Vector3f> extractPolygonFromTriangles(float[] vertices) {
		List<Vector3f> polygon = new ArrayList<>();
		for (int i = 0; i < vertices.length; i += 3) {
			polygon.add(new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]));
		}
		return removeDuplicates(polygon);
	}
	
	private List<Vector3f> removeDuplicates(List<Vector3f> list) {
		List<Vector3f> out = new ArrayList<>();
		for (Vector3f v : list) {
			boolean found = false;
			for (Vector3f o : out) {
				if (distance(o, v) < 0.001f) {
					found = true;
					break;
				}
			}
			if (!found) {
				out.add(v);
			}
		}
		return out;
	}
	
	private float distance(Vector3f a, Vector3f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	private List<Vector3f> bevelCorner(Vector3f previous, Vector3f corner, Vector3f next) {
		Vector2f directionA = new Vector2f(previous.x - corner.x, previous.y - corner.y).normalize();
		Vector2f directionB = new Vector2f(next.x - corner.x, next.y - corner.y).normalize();
		
		float angleA = (float) Math.atan2(directionA.y, directionA.x);
		float angleB = (float) Math.atan2(directionB.y, directionB.x);
		
		while (angleB <= angleA) {
			angleB += (float)(Math.PI * 2);
		}
		float arcSpan = angleA - angleB;
		
		List<Vector3f> arcPoints = new ArrayList<>();
		for (int i = 0; i <= segments; i++) {
			float t = i / (float) segments;
			float angle = angleB + t * arcSpan;
			
			float x = corner.x - (float) Math.cos(angle) * bevelAmount;
			float y = corner.y - (float) Math.sin(angle) * bevelAmount;
			arcPoints.add(new Vector3f(x, y, corner.z));
		}
		
		return arcPoints;
	}
	
	@Override
	public float[] modifyUVs(float[] oldVertices, float[] newVertices, float[] uvArray) {
		if (segments < 1 || bevelAmount == 0) {
			return uvArray;
		}
		
		List<Float> newUVs = new ArrayList<>();
		
		for (int i = 0; i < newVertices.length; i += 2) {
			float u = newVertices[i];
			float v = newVertices[i + 1];
			
			newUVs.add(u);
			newUVs.add(v);
		}
		
		normalizeUVs(newUVs);
		
		float[] newUVArray = new float[newUVs.size()];
		for (int i = 0; i < newUVArray.length; i++) {
			newUVArray[i] = newUVs.get(i);
		}
		return newUVArray;
	}
	
	private void normalizeUVs(List<Float> uvs) {
		float minU = Float.MAX_VALUE;
		float maxU = -Float.MAX_VALUE;
		float minV = Float.MAX_VALUE;
		float maxV = -Float.MAX_VALUE;
		
		for (int i = 0; i < uvs.size(); i += 2) {
			float u = uvs.get(i);
			float v = uvs.get(i + 1);
			
			if (u < minU) {
				minU = u;
			}
			if (u > maxU) {
				maxU = u;
			}
			if (v < minV) {
				minV = v;
			}
			if (v > maxV) {
				maxV = v;
			}
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
		if (bevelAmount == this.bevelAmount) {
			return;
		}
		
		this.bevelAmount = bevelAmount;
		markAsDirty();
	}
	
	public void setAutoSegments() {
		setSegments(Math.round(bevelAmount / 2f));
	}
	
	public void setSegments(int segments) {
		if (segments == this.segments) {
			return;
		}
		
		this.segments = segments;
		markAsDirty();
	}
	
}
