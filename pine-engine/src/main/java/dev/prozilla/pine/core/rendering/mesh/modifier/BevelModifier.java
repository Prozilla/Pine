package dev.prozilla.pine.core.rendering.mesh.modifier;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BevelModifier extends MeshModifier {
	
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
	public ModifiedMesh apply(float[] vertices, float[] uvArray, int[] triangles) {
		if (segments < 1 || bevelAmount == 0) {
			return new ModifiedMesh(vertices, uvArray, triangles);
		}
		
		List<Vector3f> polygon = extractPolygonFromTriangles(vertices, triangles);
		
		List<Vector3f> beveledPolygon = new ArrayList<>();
		int n = polygon.size();
		
		for (int i = 0; i < n; i++) {
			Vector3f prev = polygon.get((i - 1 + n) % n);
			Vector3f curr = polygon.get(i);
			Vector3f next = polygon.get((i + 1) % n);
			
			beveledPolygon.addAll(bevelCorner(prev, curr, next));
		}
		
		Vector3f center = computeCentroid(beveledPolygon);
		int beveledCount = beveledPolygon.size();
		
		float[] newVertices = new float[(beveledCount + 1) * 3];
		newVertices[0] = center.x;
		newVertices[1] = center.y;
		newVertices[2] = center.z;
		for (int i = 0; i < beveledCount; i++) {
			Vector3f p = beveledPolygon.get(i);
			newVertices[(i + 1) * 3] = p.x;
			newVertices[(i + 1) * 3 + 1] = p.y;
			newVertices[(i + 1) * 3 + 2] = p.z;
		}
		
		int[] newTriangles = new int[beveledCount * 3];
		for (int i = 0; i < beveledCount; i++) {
			newTriangles[i * 3] = 0;
			newTriangles[i * 3 + 1] = i + 1;
			newTriangles[i * 3 + 2] = (i + 1) % beveledCount + 1;
		}
		
		float[] newUVs = generateUVs(newVertices);
		
		return new ModifiedMesh(newVertices, newUVs, newTriangles);
	}
	
	private float[] generateUVs(float[] newVertices) {
		int vertexCount = newVertices.length / 3;
		float[] newUVs = new float[vertexCount * 2];
		for (int i = 0; i < vertexCount; i++) {
			newUVs[i * 2] = newVertices[i * 3];
			newUVs[i * 2 + 1] = newVertices[i * 3 + 1];
		}
		
		normalizeUVs(newUVs);
		return newUVs;
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
	
	private List<Vector3f> extractPolygonFromTriangles(float[] vertices, int[] triangles) {
		List<Vector3f> polygon = new ArrayList<>();
		for (int i = 0; i < triangles.length; i++) {
			int vertex = triangles[i];
			polygon.add(new Vector3f(vertices[vertex * 3], vertices[vertex * 3 + 1], vertices[vertex * 3 + 2]));
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
	
	private void normalizeUVs(float[] uvs) {
		float minU = Float.MAX_VALUE;
		float maxU = -Float.MAX_VALUE;
		float minV = Float.MAX_VALUE;
		float maxV = -Float.MAX_VALUE;
		
		for (int i = 0; i < uvs.length; i += 2) {
			float u = uvs[i];
			float v = uvs[i + 1];
			
			if (u < minU) minU = u;
			if (u > maxU) maxU = u;
			if (v < minV) minV = v;
			if (v > maxV) maxV = v;
		}
		
		float scaleU = maxU - minU;
		float scaleV = maxV - minV;
		
		for (int i = 0; i < uvs.length; i += 2) {
			uvs[i] = (uvs[i] - minU) / scaleU;
			uvs[i + 1] = (uvs[i + 1] - minV) / scaleV;
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
