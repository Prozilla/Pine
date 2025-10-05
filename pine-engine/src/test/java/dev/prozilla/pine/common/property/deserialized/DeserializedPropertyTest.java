package dev.prozilla.pine.common.property.deserialized;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.test.MockLogger;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeserializedPropertyTest {

	@Test
	void test() {
		FileDeserializer<Data> deserializer = new FileDeserializer<>("data/data.json", Data.class);
		deserializer.setLogger(new MockLogger());
		
		DeserializedProperty<Color> property = deserializer.createProperty((data) -> data.color);
		assertTrue(property.exists(), "DeserializedProperty should be initialized with a value");
		assertEquals(Color.decode("#FF0000"), property.getValue(),
			"DeserializedProperty should take value from the external file");
		
		Data newData = new Data();
		newData.color = new Color(255, 0, 0);
		deserializer.setValue(newData);
		
		assertEquals(newData.color, property.getValue(),
			"DeserializedProperty should reflect changes to the value of the deserializer");
	}

}
