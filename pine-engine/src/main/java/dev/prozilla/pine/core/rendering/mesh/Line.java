package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.ListUtils;
import dev.prozilla.pine.common.util.ObservableArrayList;
import dev.prozilla.pine.common.util.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Line extends Mesh {
	
	protected final ObservableList<Vector2f> points;
	protected float[] thickness;
	protected boolean isClosed;
	
	private static final float EPSILON = 1e-6f;
	
	public Line() {
		this(new Vector3f());
	}
	
	public Line(Vector3f origin) {
		this(origin, null);
	}
	
	public Line(Vector3f origin, List<Vector2f> points) {
		this(origin, points, 1f);
	}
	
	public Line(Vector3f origin, List<Vector2f> points, float thickness) {
		this(origin, points, new float[]{ thickness });
	}
	
	public Line(Vector3f origin, List<Vector2f> points, float[] thickness) {
		super(origin);
		this.points = new ObservableArrayList<>();
		this.thickness = thickness;
		
		if (points != null) {
			this.points.addAll(points);
		}
		
		this.points.onUpdate(this::onPointsUpdate);
	}
	
	private void onPointsUpdate(ObservableList.Event<Vector2f> event) {
		markAsDirty();
	}
	
	public void setThickness(float[] thickness) {
		if (Arrays.equals(this.thickness, thickness)) {
			return;
		}
		
		this.thickness = thickness;
		markAsDirty();
	}
	
	public int getPointCount() {
		return points.size();
	}
	
	public void setPoints(List<Vector2f> points) {
		if (Objects.equals(this.points, points)) {
			return;
		}
		
		this.points.clear();
		this.points.addAll(points);
	}
	
	public void addPoints(List<Vector2f> points) {
		this.points.addAll(points);
	}
	
	public Vector2f getPoint(int index) {
		return points.get(index);
	}
	
	public void setPoint(int index, Vector2f point) {
		if (Objects.equals(points.get(index), point)) {
			return;
		}
		
		points.set(index, point);
	}
	
	public void addPoint(Vector2f point) {
		points.add(point);
	}
	
	public void removePoint(int index) {
		points.remove(index);
	}
	
	public boolean isClosed() {
		return isClosed;
	}
	
	public void setClosed(boolean closed) {
		if (this.isClosed == closed) {
			return;
		}
		
		isClosed = closed;
		markAsDirty();
	}
	
	@Override
	protected float[] generateVertices() {
		int totalPoints = points.size();
		
		if (totalPoints < 2)
			return null;
		
		float[] vertices = new float[totalPoints * 6]; // 2 vertices per point
		
		for (int i = 0; i < totalPoints; i++) {
			Vector2f point = points.get(i);
			float pointOffset = thickness[i % thickness.length] / 2f;
			
			vertices[i * 6] = origin.x + point.x;
			vertices[i * 6 + 1] = origin.y + point.y;
			vertices[i * 6 + 2] = origin.z;
			vertices[i * 6 + 3] = origin.x + point.x;
			vertices[i * 6 + 4] = origin.y + point.y;
			vertices[i * 6 + 5] = origin.z;
			
			if (i == 0 && !isClosed) {
				Vector2f nextPoint = points.get(i + 1);
				Vector2f direction = nextPoint.clone().subtract(point);
				Vector2f perpendicular = new Vector2f(-direction.y, direction.x).normalize().scale(pointOffset);
				
				vertices[0] += perpendicular.x;
				vertices[1] += perpendicular.y;
				vertices[3] -= perpendicular.x;
				vertices[4] -= perpendicular.y;
			} else if (i == totalPoints - 1 && !isClosed) {
				Vector2f previousPoint = points.get(i - 1);
				Vector2f direction = previousPoint.clone().subtract(point);
				Vector2f perpendicular = new Vector2f(-direction.y, direction.x).normalize().scale(pointOffset);
				
				vertices[i * 6] -= perpendicular.x;
				vertices[i * 6 + 1] -= perpendicular.y;
				vertices[i * 6 + 3] += perpendicular.x;
				vertices[i * 6 + 4] += perpendicular.y;
			} else {
				Vector2f previousPoint = ListUtils.getCircular(points, i - 1);
				Vector2f nextPoint = ListUtils.getCircular(points, i + 1);
				
				Vector2f previousDirection = previousPoint.clone().subtract(point).normalize();
				Vector2f nextDirection = nextPoint.clone().subtract(point).normalize();
				
				Vector2f nextDirectionPerpendicular = new Vector2f(-nextDirection.y, nextDirection.x).normalize();
				
				Vector2f tangent = previousDirection.clone().add(nextDirection);
				Vector2f bisector;
				
				if (tangent.lengthSquared() < EPSILON) {
					bisector = nextDirectionPerpendicular.scale(pointOffset);
				} else {
					Vector2f miterDirection = tangent.normalize();
					float dot = miterDirection.dot(nextDirectionPerpendicular);
					
					if (Math.abs(dot) < EPSILON) {
						bisector = nextDirectionPerpendicular.scale(pointOffset);
					} else {
						bisector = miterDirection.scale(pointOffset / dot);
					}
				}
				
				vertices[i * 6] += bisector.x;
				vertices[i * 6 + 1] += bisector.y;
				vertices[i * 6 + 3] -= bisector.x;
				vertices[i * 6 + 4] -= bisector.y;
			}
		}
		
		return vertices;
	}
	
	@Override
	protected float[] generateUVs() {
		int totalPoints = points.size();
		
		float[] vertices = getVertices();
		float xMin = vertices[0];
		float xMax = vertices[0];
		float yMin = vertices[0];
		float yMax = vertices[0];
		
		for (int i = 1; i < totalPoints * 2; i++) {
			float x = vertices[i * 3];
			float y = vertices[i * 3 + 1];
			
			xMin = Math.min(xMin, x);
			xMax = Math.max(xMax, x);
			yMin = Math.min(yMin, y);
			yMax = Math.max(yMax, y);
		}
		
		float[] uvArray = new float[totalPoints * 4];
		
		for (int i = 0; i < totalPoints * 2; i++) {
			uvArray[i * 2] = MathUtils.remap(vertices[i * 3], xMin, xMax, 0, 1);
			uvArray[i * 2 + 1] = MathUtils.remap(vertices[i * 3 + 1], yMin, yMax, 0, 1);
		}
		
		return uvArray;
	}
	
	@Override
	protected int[] generateTriangles() {
		int totalPoints = points.size();
		int triangleCount = totalPoints * 2;
		if (!isClosed) {
			triangleCount -= 2;
		}
		
		int[] triangles = new int[triangleCount * 3];
		
		for (int i = 0; i < totalPoints - 1; i++) {
			triangles[i * 6] = i * 2;
			triangles[i * 6 + 1] = i * 2 + 1;
			triangles[i * 6 + 2] = i * 2 + 3;
			triangles[i * 6 + 3] = i * 2;
			triangles[i * 6 + 4] = i * 2 + 3;
			triangles[i * 6 + 5] = i * 2 + 2;
		}
		
		if (isClosed) {
			int lastPoint = totalPoints - 1;
			int previousPoint = lastPoint - 1;
			triangles[lastPoint * 6] = previousPoint * 2 + 2;
			triangles[lastPoint * 6 + 1] = previousPoint * 2 + 3;
			triangles[lastPoint * 6 + 2] = 1;
			triangles[lastPoint * 6 + 3] = previousPoint * 2 + 2;
			triangles[lastPoint * 6 + 4] = 1;
			triangles[lastPoint * 6 + 5] = 0;
		}
		
		return triangles;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Line line && equals(line));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Line line && equals(line));
	}
	
	public boolean equals(Line line) {
		return line != null && Objects.equals(line.origin, origin) && line.thickness == thickness && Objects.equals(line.points, points);
	}
	
	@Override
	public Line clone() {
		return new Line(origin, points, thickness);
	}
	
	public static List<Vector2f> pointsOnRect(float x, float y, float width, float height) {
		List<Vector2f> points = new ArrayList<>();
		points.add(new Vector2f(x, y));
		points.add(new Vector2f(x, y + height));
		points.add(new Vector2f(x + width, y + height));
		points.add(new Vector2f(x + width, y));
		return points;
	}
}
