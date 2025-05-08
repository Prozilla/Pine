package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.util.Parser;

import java.util.ArrayList;

public class DualDimensionParser extends Parser<DualDimension> {
	
	@Override
	public boolean parse(String input) {
		String[] sections = input.trim().split("\\s+");
		ArrayList<DimensionBase> dimensions = new ArrayList<>();
		
		DimensionParser dimensionParser = new DimensionParser();
		for (String section : sections) {
			if (!dimensionParser.parse(section)) {
				return fail(dimensionParser.getError());
			}
			dimensions.add(dimensionParser.getResult());
		}
		
		DualDimension dualDimension = switch (dimensions.size()) {
			case 1 -> new DualDimension(dimensions.get(0));
			case 2 -> new DualDimension(dimensions.get(0), dimensions.get(1));
			default -> null;
		};
		
		if (dualDimension == null) {
			return fail();
		}
		
		return succeed(dualDimension);
	}
}
