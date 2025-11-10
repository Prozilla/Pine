package dev.prozilla.pine.common.property.deserialized;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.prozilla.pine.common.util.parser.Parser;

import java.io.IOException;

/**
 * A JSON deserializer that deserializes values by using a parser.
 */
public class ValueDeserializer<T> extends JsonDeserializer<T> {
	
	private final Class<T> type;
	private final Parser<T> parser;
	
	public ValueDeserializer(Class<T> type, Parser<T> parser) {
		this.type = type;
		this.parser = parser;
	}
	
	@Override
	public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String input = jsonParser.getValueAsString();
		return parser.read(input);
	}
	
	public Class<T> getType() {
		return type;
	}
	
}
