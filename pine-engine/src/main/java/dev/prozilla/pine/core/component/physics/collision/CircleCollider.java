package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.Experimental;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.mesh.Circle;

public class CircleCollider extends Collider {
	
	public float radius;
	
	public CircleCollider(float radius) {
		this(radius, new Vector3f());
	}
	
	public CircleCollider(float radius, Vector3f offset) {
		super(offset);
		this.radius = radius;
	}
	
	@Experimental
	public boolean collidesWith(CircleCollider other) {
		Pine.useExperimentalFeature();
		float x1 = getOriginX();
		float y1 = getOriginY();
		float x2 = other.getOriginX();
		float y2 = other.getOriginY();
		return new Vector2f(x1, y1).distance(x2, y2) <= radius + other.radius;
	}
	
	@Experimental
	public boolean collidesWith(RectCollider other) {
		Pine.useExperimentalFeature();
		float circleX = getOriginX();
		float circleY = getOriginY();
		
		// Calculate the closest point on the rectangle to the circle's center
		float rectX = MathUtils.clamp(circleX, other.getLeft(), other.getRight());
		float rectY = MathUtils.clamp(circleY, other.getBottom(), other.getTop());
		
		// Calculate vector from center of circle to the closest point on rect
		float deltaX = rectX - circleX;
		float deltaY = rectY - circleY;
		
		// Normalize delta vector
		float deltaLength = MathUtils.sqrt(MathUtils.square(deltaX) + MathUtils.square(deltaY));
		deltaX /= deltaLength;
		deltaY /=  deltaLength;
		
		// Calculate point on the intersection between this circle and
		// the line connecting the centers of this circle and the rect
		float x = circleX + deltaX * radius;
		float y = circleY + deltaY * radius;
		
		// Check if point is inside rect
		return other.isInside(x, y);
	}
	
	@Experimental
	@Override
	public boolean collidesWith(Collider other) {
		return other.collidesWith(this);
	}
	
	@Override
	public void draw(Renderer renderer, Color color) {
		Circle circle = new Circle(getOrigin(), radius);
		circle.draw(renderer, color);
	}
	
}
