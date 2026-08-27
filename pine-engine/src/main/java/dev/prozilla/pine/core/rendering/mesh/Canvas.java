package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.function.mapper.FloatMapper;
import dev.prozilla.pine.common.util.function.mapper.FromFloatMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Canvas extends Mesh {
	
	protected final List<Mesh> meshes;
	protected Mesh combinedMesh;
	
	private Path path;
	
	private State state;
	private final Stack<State> stack;
	
	public Canvas() {
		this(new Vector3f());
	}
	
	public Canvas(Vector3f origin) {
		super(origin);
		
		meshes = new ArrayList<>();
		
		path = new Path();
		
		state = new State();
		stack = new Stack<>();
	}
	
	public Canvas setLineWidth(float lineWidth) {
		if (lineWidth <= 0) {
			return this;
		}
		state.lineWidth = lineWidth;
		return this;
	}
	
	public Canvas parametricCurve(FromFloatMapper<Vector2f> function, float start, float end) {
		return parametricCurve(function, start, end, Math.round(Math.abs(end - start) / 2f));
	}
	
	public Canvas parametricCurve(FromFloatMapper<Vector2f> function, float start, float end, int resolution) {
		Vector2f startPoint = function.map(start);
		moveTo(startPoint);
		
		int points = Math.max(resolution, 0) + 2;
		for (int i = 1; i < points; i++) {
			float t = start + (i * (end - start)) / (points - 1);
			Vector2f point = function.map(t);
			lineTo(point);
		}
		
		return this;
	}
	
	public Canvas functionCurve(FloatMapper function, float startX, float endX) {
		return functionCurve(function, startX, endX, Math.round(Math.abs(endX - startX) / 2f));
	}
	
	public Canvas functionCurve(FloatMapper function, float startX, float endX, int resolution) {
		float startY = function.mapToFloat(startX);
		moveTo(new Vector2f(startX, startY));
		
		int points = Math.max(resolution, 0) + 2;
		for (int i = 1; i < points; i++) {
			float x = startX + (i * (endX - startX)) / (points - 1f);
			float y = function.mapToFloat(x);
			lineTo(new Vector2f(x, y));
		}
		
		return this;
	}
	
	public Canvas beginPath() {
		path.reset();
		return this;
	}
	
	public Canvas moveTo(Vector2f point) {
		path.moveTo(point);
		return this;
	}
	
	public Canvas closePath() {
		path.closePath();
		return this;
	}
	
	public Canvas lineTo(Vector2f point) {
		path.lineTo(point);
		return this;
	}
	
	public Canvas rect(Vector2f origin, Vector2f size) {
		path.rect(origin, size);
		return this;
	}
	
	public Canvas stroke() {
		List<Mesh> strokeMeshes = path.stroke(state);
		if (strokeMeshes.isEmpty()) {
			return this;
		}
		
		meshes.addAll(strokeMeshes);
		markAsDirty();
		return this;
	}
	
	public Canvas save() {
		stack.push(state.clone());
		return this;
	}
	
	public Canvas restore() {
		if (!stack.isEmpty()) {
			state = stack.pop();
		}
		return this;
	}
	
	public Canvas reset() {
		meshes.clear();
		path.reset();
		state = new State();
		stack.clear();
		markAsDirty();
		return this;
	}
	
	@Override
	protected float[] generateVertices() {
		if (meshes.isEmpty()) {
			return null;
		}
		
		combinedMesh = meshes.getFirst();
		for (int i = 1; i < meshes.size(); i++) {
			Mesh mesh = meshes.get(i);
			combinedMesh = combinedMesh.join(mesh);
		}
		
		return combinedMesh.getVertices();
	}
	
	@Override
	protected float[] generateUVs() {
		return combinedMesh.getUVArray();
	}
	
	@Override
	protected int[] generateTriangles() {
		return combinedMesh.getTriangles();
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Canvas canvas && equals(canvas));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Canvas canvas && equals(canvas));
	}
	
	public boolean equals(Canvas canvas) {
		return canvas != null && Objects.equals(canvas.origin, origin) && Objects.equals(canvas.meshes, meshes);
	}
	
	@Override
	public Canvas clone() {
		Canvas canvas = new Canvas(origin);
		canvas.meshes.addAll(meshes);
		return canvas;
	}
	
	private static class State implements Cloneable<State> {
	
		protected float lineWidth;
		
		public State() {
			lineWidth = 1;
		}
		
		@Override
		public boolean equals(Object object) {
			return object == this || (object instanceof State state && equals(state));
		}
		
		@Override
		public boolean equals(State state) {
			return state != null && lineWidth == state.lineWidth;
		}
		
		@Override
		public State clone() {
			State state = new State();
			state.lineWidth = lineWidth;
			return state;
		}
	}
	
	private static class Path {
	
		protected List<Subpath> subpaths;
		protected boolean needsNewSubpath;
		
		public Path() {
			subpaths = new ArrayList<>();
			needsNewSubpath = true;
		}
		
		public void moveTo(Vector2f point) {
			createSubpath(point);
		}
		
		public void closePath() {
			Subpath subpath = getLastSubpath();
			if (subpath == null) {
				return;
			}
			
			subpath.close();
			createSubpath(subpath.getFirstPoint().clone());
		}
		
		public void lineTo(Vector2f point) {
			if (!ensureSubpath(point)) {
				Subpath subpath = getLastSubpath();
				if (subpath != null) {
					subpath.addPoint(point);
				}
			}
		}
		
		public void rect(Vector2f origin, Vector2f size) {
			createSubpath(origin)
				.addPoint(new Vector2f(origin.x + size.x, origin.y))
				.addPoint(new Vector2f(origin.x + size.x, origin.y + size.y))
				.addPoint(new Vector2f(origin.x, origin.y + size.y))
				.close();
			createSubpath(origin);
		}
		
		protected boolean ensureSubpath(Vector2f point) {
			if (needsNewSubpath) {
				createSubpath(point);
				return true;
			}
			return false;
		}
		
		protected Subpath getLastSubpath() {
			if (subpaths.isEmpty()) {
				return null;
			}
			return subpaths.getLast();
		}
		
		protected Subpath createSubpath(Vector2f start) {
			Subpath subpath = new Subpath(start);
		    subpaths.add(subpath);
			needsNewSubpath = false;
			return subpath;
		}
		
		public List<Mesh> stroke(State state) {
			List<Mesh> meshes = new ArrayList<>();
			for (Subpath subpath : subpaths) {
				if (subpath.isValid()) {
					meshes.add(subpath.stroke(state));
				}
			}
			return meshes;
		}
		
		public void reset() {
			subpaths.clear();
			needsNewSubpath = true;
		}
	}
	
	private static class Subpath {
	
		protected List<Vector2f> points;
		protected boolean isClosed;
		
		public Subpath(Vector2f start) {
			points = new ArrayList<>();
			points.add(start);
			isClosed = false;
		}
	
		public Subpath addPoint(Vector2f point) {
			points.add(point);
			return this;
		}
		
		public Vector2f getFirstPoint() {
			return points.getFirst();
		}
		
		public Vector2f getLastPoint() {
			return points.getLast();
		}
		
		public Subpath close() {
			isClosed = true;
			return this;
		}
		
		public Mesh stroke(State state) {
			List<Vector2f> points = new ArrayList<>(this.points);
			if (isClosed) {
				points.add(points.getFirst());
			}
			
			return new Line(new Vector3f(), points, state.lineWidth);
		}
		
		public boolean isValid() {
			return points.size() > 1;
		}
	}
}
