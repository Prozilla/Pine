package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.util.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class DimensionParser extends Parser<DimensionBase> {
	
	@Override
	public boolean parse(String input) {
		if (input.isBlank()) {
			return fail("input must not be blank");
		}
		
		input = input.trim().toLowerCase();
		
		// Find index of first non-digit of string
		int splitIndex = 0;
		
		// Skip sign
		if (input.charAt(splitIndex) == '-') {
			splitIndex++;
		}
		
		int inputLength = input.length();
		Character currentChar = null;
		Character nextChar = null;
		splitIndex--;
		
		do {
			splitIndex++;
			
			if (splitIndex < inputLength) {
				currentChar = input.charAt(splitIndex);
				if (splitIndex + 1 < inputLength) {
					nextChar = input.charAt(splitIndex + 1);
				}
			}
		} while (splitIndex < inputLength && (
			Character.isDigit(currentChar)
				|| currentChar == '.' && nextChar != null && Character.isDigit(nextChar))
		);
		
		// If input string starts with non-digit, parse it as a dimension function
		if (splitIndex == 0) {
			if (input.equals("auto")) {
				return succeed(Dimension.auto());
			}
			
			List<String> args = new ArrayList<>(List.of(input.split("\\s*[(,)]\\s*")));
			String functionName = args.removeFirst();
			
			// Parse each argument
			List<DimensionBase> dimensions = new ArrayList<>();
			DimensionParser dimensionParser = new DimensionParser();
			for (String arg : args) {
				if (arg.isBlank()) {
					continue;
				}
				
				if (!dimensionParser.parse(arg)) {
					return fail("input contains invalid nested dimensions");
				} else {
					dimensions.add(dimensionParser.getResult());
				}
			}
			int argCount = dimensions.size();
			
			if (argCount == 0) {
				return fail("input contains dimension function without arguments");
			}
			
			DimensionBase[] dimensionsArray = dimensions.toArray(new DimensionBase[0]);
			
			DimensionBase dimension = switch (functionName) {
				case "max" -> Dimension.max(dimensionsArray);
				case "min" -> Dimension.min(dimensionsArray);
				case "clamp" -> (argCount == 3) ? Dimension.clamp(dimensions.get(0), dimensions.get(1), dimensions.get(2)) : null;
				case "add" -> Dimension.add(dimensionsArray);
				case "multiply" -> Dimension.multiply(dimensionsArray);
				default -> null;
			};
			
			if (dimension == null) {
				return fail("input contains unknown function name");
			} else {
				return succeed(dimension);
			}
		} else {
			// Split input into value (digits) and unit (non-digits)
			String valueString = input.substring(0, splitIndex);
			String unitString = input.substring(splitIndex);
			
			if (!unitString.isBlank() && !Unit.isValid(unitString)) {
				return fail("input contains invalid unit");
			}
			
			float value = Float.parseFloat(valueString);
			Unit unit = unitString.isBlank() ? Unit.PIXELS : Unit.parse(unitString);
			
			return succeed(new Dimension(value, unit));
		}
	}
}
