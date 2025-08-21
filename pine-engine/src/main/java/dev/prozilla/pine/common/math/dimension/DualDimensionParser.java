package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.property.selection.WrapMode;
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
		
		WrapMode.REPEAT.resizeList(dimensions, 2);
		return succeed(new DualDimension(dimensions.getFirst(), dimensions.get(1)));
	}
}
