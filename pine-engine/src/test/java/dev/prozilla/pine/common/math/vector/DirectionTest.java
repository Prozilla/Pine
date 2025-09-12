package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DirectionTest {
	
	@Test
	void testRotation() {
		Direction original = Direction.UP;
		Direction expected = Direction.DOWN;
		Direction actual = original.rotateClockwise() // Right
			.opposite()                               // Left
            .rotateCounterclockwise() // Down
            .rotateCounterclockwise() // Right
            .rotateClockwise() // Down
            .opposite() // Up
            .opposite() // Down
            .rotateClockwise() // Left
	        .rotateClockwise() // Up
	        .rotateClockwise() // Right
	        .rotateClockwise() // Down
            .rotateCounterclockwise() // Right
			.rotateCounterclockwise() // Up
			.rotateCounterclockwise() // Left
			.rotateCounterclockwise(); // Down
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testClockwiseDegrees() {
		for (Direction direction : Direction.values()) {
			Direction next = direction.rotateClockwise();
			float angle = Math.abs(next.degrees - direction.degrees) % 180;
			
			assertEquals(90, angle, "angle of clockwise rotation should be 90 degrees");
		}
	}
	
	@Test
	void testCounterclockwiseDegrees() {
		for (Direction direction : Direction.values()) {
			Direction next = direction.rotateCounterclockwise();
			float angle = Math.abs(next.degrees - direction.degrees) % 180;
			
			assertEquals(90, angle, "angle of counterclockwise rotation should be 90 degrees");
		}
	}
	
	@Test
	void testOppositeVectors() {
		for (Direction direction : Direction.values()) {
			Direction opposite = direction.opposite();
			Vector2i sum = direction.toIntVector().add(opposite.toIntVector());
			
			assertEquals(new Vector2i(0, 0), sum, "sum of vectors of opposite directions should be (0, 0)");
		}
	}
	
	@Test
	void testIntVectorToDirection() {
		for (Direction direction : Direction.values()) {
			Direction actual = Direction.fromIntVector(direction.toIntVector());
			
			assertEquals(direction, actual);
		}
		
		assertNull(Direction.fromIntVector(new Vector2i(1, 1)));
		assertNull(Direction.fromIntVector(new Vector2i(0, 0)));
	}
	
	@Test
	void testFloatVectorToDirection() {
		for (Direction direction : Direction.values()) {
			Direction actual = Direction.fromFloatVector(direction.toFloatVector());
			
			assertEquals(direction, actual);
		}
		
		assertNull(Direction.fromFloatVector(new Vector2f(1, 1)));
		assertNull(Direction.fromFloatVector(new Vector2f(0, 0)));
	}
	
}
