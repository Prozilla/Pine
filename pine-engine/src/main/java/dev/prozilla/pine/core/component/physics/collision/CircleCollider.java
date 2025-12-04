package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shape.Circle;

public class CircleCollider extends Collider {
	
	public float radius;
	
	public CircleCollider(float radius) {
		this(radius, new Vector2f());
	}
	
	public CircleCollider(float radius, Vector2f offset) {
		super(offset);
		this.radius = radius;
	}
	
	public boolean collidesWith(CircleCollider other) {
		float x1 = getOriginX();
		float y1 = getOriginY();
		float x2 = other.getOriginX();
		float y2 = other.getOriginY();
		return Vector2f.distance(x1, y1, x2, y2) <= radius + other.radius;
	}
	
	public boolean collidesWith(RectCollider other) {
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
	
	@Override
	public boolean collidesWith(Collider other) {
		return other.collidesWith(this);
	}
	
	@Override
	public void draw(Renderer renderer, Color color, float depth) {
		Vector2f position = getScene().getCameraData().applyTransform(getOrigin());
		Circle circle = new Circle(position, radius);
		circle.draw(renderer, color, depth);
	}
	
}
