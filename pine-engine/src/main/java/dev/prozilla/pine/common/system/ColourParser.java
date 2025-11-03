package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.util.parser.Parser;

public class ColourParser extends Parser<Colour> {
	
	private final ColorParser colorParser;
	
	public ColourParser() {
		colorParser = new ColorParser();
	}
	
	@Override
	public boolean parse(String input) {
		if (colorParser.parse(input)) {
			return succeed(colorParser.getResult().toColour());
		} else {
			return fail(colorParser.getError());
		}
	}
	
}
