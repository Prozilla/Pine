package dev.prozilla.pine.core.rendering.shape.modifier;

import dev.prozilla.pine.common.math.vector.Vector2f;

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
		List<Vector2f> polygon = extractPolygonFromTriangles(vertices);
		
		// Bevel each corner
		List<Vector2f> beveledPolygon = new ArrayList<>();
		int n = polygon.size();
		
		for (int i = 0; i < n; i++) {
			Vector2f prev = polygon.get((i - 1 + n) % n);
			Vector2f curr = polygon.get(i);
			Vector2f next = polygon.get((i + 1) % n);
			
			List<Vector2f> cornerPoints = bevelCorner(prev, curr, next);
			beveledPolygon.addAll(cornerPoints);
		}
		
		// Triangulate using center-fan approach
		Vector2f center = computeCentroid(beveledPolygon);
		List<Float> newVertices = new ArrayList<>();
		
		for (int i = 0; i < beveledPolygon.size(); i++) {
			Vector2f p1 = beveledPolygon.get(i);
			Vector2f p2 = beveledPolygon.get((i + 1) % beveledPolygon.size());
			
			newVertices.add(center.x);
			newVertices.add(center.y);
			
			newVertices.add(p1.x);
			newVertices.add(p1.y);
			
			newVertices.add(p2.x);
			newVertices.add(p2.y);
		}
		
		float[] out = new float[newVertices.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = newVertices.get(i);
		}
		return out;
	}
	
	private Vector2f computeCentroid(List<Vector2f> points) {
		Vector2f centroid = new Vector2f();
		for (Vector2f point : points) {
			centroid.x += point.x;
			centroid.y += point.y;
		}
		return centroid.divide(points.size());
	}
	
	private List<Vector2f> extractPolygonFromTriangles(float[] vertices) {
		List<Vector2f> polygon = new ArrayList<>();
		for (int i = 0; i < vertices.length; i += 2) {
			polygon.add(new Vector2f(vertices[i], vertices[i + 1]));
		}
		return removeDuplicates(polygon);
	}
	
	private List<Vector2f> removeDuplicates(List<Vector2f> list) {
		List<Vector2f> out = new ArrayList<>();
		for (Vector2f v : list) {
			boolean found = false;
			for (Vector2f o : out) {
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
	
	private float distance(Vector2f a, Vector2f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	private List<Vector2f> bevelCorner(Vector2f previous, Vector2f corner, Vector2f next) {
		Vector2f directionA = new Vector2f(previous.x - corner.x, previous.y - corner.y).normalize();
		Vector2f directionB = new Vector2f(next.x - corner.x, next.y - corner.y).normalize();
		
		float angleA = (float) Math.atan2(directionA.y, directionA.x);
		float angleB = (float) Math.atan2(directionB.y, directionB.x);
		
		while (angleB <= angleA) {
			angleB += (float)(Math.PI * 2);
		}
		float arcSpan = angleA - angleB;
		
		List<Vector2f> arcPoints = new ArrayList<>();
		for (int i = 0; i <= segments; i++) {
			float t = i / (float) segments;
			float angle = angleB + t * arcSpan;
			
			float x = corner.x - (float) Math.cos(angle) * bevelAmount;
			float y = corner.y - (float) Math.sin(angle) * bevelAmount;
			arcPoints.add(new Vector2f(x, y));
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
