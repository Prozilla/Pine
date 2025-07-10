package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.util.Parser;

public class ColourParser extends Parser<Colour> {
	
	private final ColorParser colorParser;
	
	public ColourParser() {
		colorParser = new ColorParser();
	}
	
	@Override
	public boolean parse(String input) {
		boolean succeeded = colorParser.parse(input);
		
		if (succeeded) {
			return succeed(colorParser.getResult().toColour());
		} else {
			return fail(colorParser.getError());
		}
	}
	
}
