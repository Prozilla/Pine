package vector;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestExtension.class)
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
	
}
