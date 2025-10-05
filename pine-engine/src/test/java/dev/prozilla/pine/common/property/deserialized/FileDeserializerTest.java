package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.test.MockLogger;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileDeserializerTest {

	@Test
	void testDeserialization() {
		FileDeserializer<Data> deserializer = new FileDeserializer<>("data/data.json", Data.class);
		deserializer.setLogger(new MockLogger());
		
		Data data = deserializer.deserialize();
		assertNotNull(data, "deserialized data should not null");
		assertEquals("This is a string", data.string);
		assertEquals(10, data.integer);
		assertNull( data.empty);
		assertArrayEquals(new String[]{"one", "two", "three"}, data.array);
		assertTrue(data.bool);
		assertEquals(new Vector2i(1, 2), data.vector2i);
		assertEquals(new Vector4f(3, 2, 3, 2), data.vector4f);
		assertEquals(Color.decode("#FF0000"), data.color);
		
		assertEquals(data, deserializer.getValue(),
			"value of FileDeserializer should be set whenever deserialize() is called");
	}
	
}
