package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CircleColliderTest {

	@Test
	void testCircleNextToRect() {
		CircleCollider circleCollider = new MockedCircleCollider(5f);
		RectCollider rectCollider = new MockedRectCollider(new Vector2f(10, 10), new Vector2f(6, -5));
		
		assertFalse(circleCollider.collidesWith(rectCollider), "circle should not collide with rect");
		assertFalse(rectCollider.collidesWith(circleCollider), "rect should not collide with circle");
	}
	
	@Test
	void testCircleIntersectingRect() {
		CircleCollider circleCollider = new MockedCircleCollider(5f);
		RectCollider rectCollider = new MockedRectCollider(new Vector2f(10, 10), new Vector2f(0, -5));
		
		assertTrue(circleCollider.collidesWith(rectCollider), "circle should collide with rect");
		assertTrue(rectCollider.collidesWith(circleCollider), "rect should collide with circle");
	}
	
	@Test
	void testIntersectingCircles() {
		CircleCollider a = new MockedCircleCollider(5f);
		CircleCollider b = new MockedCircleCollider(5f, new Vector2f(5, 0));
		
		assertTrue(a.collidesWith(b), "circle should collide with circle");
		assertTrue(b.collidesWith(a), "circle should collide with circle");
	}
	
	@Test
	void testCircleNextToCircle() {
		CircleCollider a = new MockedCircleCollider(5f);
		CircleCollider b = new MockedCircleCollider(5f, new Vector2f(15, 0));
		
		assertFalse(a.collidesWith(b), "circle should not collide with circle");
		assertFalse(b.collidesWith(a), "circle should not collide with circle");
	}

}
