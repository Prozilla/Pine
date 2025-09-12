package dev.prozilla.pine.common.math.easing;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.SequentialParser;

public class EasingParser extends SequentialParser<EasingFunction> {
	
	private static final Vector4f.Parser vector4fParser = new Vector4f.Parser();
	
	@Override
	public boolean parse(String input) {
		setInput(input);
		
		if (isMethodCall("cubic-bezier")) {
			String arguments = readBetweenParentheses();
			if (arguments.isBlank()) {
				return fail("Missing arguments");
			} else if (!vector4fParser.parse(arguments)) {
				return fail(vector4fParser.getError());
			}
			Vector4f vector4f = vector4fParser.getResult();
			return succeed(new CubicBezierEasing(vector4f));
		} else if (isMethodCall("steps")) {
			String argumentsString = readBetweenParentheses();
			if (argumentsString.isBlank()) {
				return fail("Missing arguments");
			}
			String[] arguments = argumentsString.split("\\s*,\\s*");
			try {
				int stepCount = Integer.parseInt(arguments[0]);
				if (arguments.length >= 2) {
					if ("start".equalsIgnoreCase(arguments[1]) || "jump-start".equalsIgnoreCase(arguments[1])) {
						return succeed(new StepEasing(stepCount, true));
					} else if ("end".equalsIgnoreCase(arguments[1]) || "jump-end".equalsIgnoreCase(arguments[1])) {
						return succeed(new StepEasing(stepCount, false));
					} else {
						return fail("Invalid argument: " + arguments[1]);
					}
				} else {
					return succeed(new StepEasing(stepCount));
				}
			} catch (NumberFormatException e) {
				return fail("Invalid argument: " + arguments[0]);
			}
		} else {
			Easing easing = ArrayUtils.findByString(Easing.values(), input, true);
			if (easing == null) {
				return fail();
			} else {
				return succeed(easing);
			}
		}
	}
	
}
