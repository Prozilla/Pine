package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.AnimationCurveParser;
import dev.prozilla.pine.common.property.style.CSSParser;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.StyledPropertyKey;
import dev.prozilla.pine.common.property.style.selector.*;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StyledPropertyTest {
	
	@Test
	void testSelectorParser() {
		SelectorParser selectorParser = new SelectorParser();
		TestUtils.testParser("div.class#id:not(:hover)", new SelectorCombo(new TypeSelector("div"), new ClassSelector("class"), new IdSelector("id"), new NotSelector(new ModifierSelector("hover"))), selectorParser);
		TestUtils.testParser("*", Selector.UNIVERSAL, selectorParser);
	}
	
	@Test
	void testStyleSheetParser() {
		StyleSheet expected = new StyleSheet();
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.COLOR, Color.decode("#fff"));
		expected.addRule(new IdSelector("id"), StyledPropertyKey.COLOR, Color.decode("#000"));
		
		CSSParser cssParser = new CSSParser();
		String input = "* { color: #fff } #id { color: #000 }";
		
		TestUtils.testParser(input, expected, cssParser);
	}
	
	@Test
	void testStyleSheetParseToString() {
		CSSParser cssParser = new CSSParser();
		String[] inputStrings = new String[] {
			":hover { color: rgba(0.25, 0.75, 0.5, 1.0); }",
			"* { color: rgba(1.0, 1.0, 1.0, 1.0); } #id { color: rgba(0.0, 0.0, 0.0, 1.0); }"
		};
	
		for (String input : inputStrings) {
			if (cssParser.parse(input)) {
				StyleSheet styleSheet = cssParser.getResult();
				assertEquals(input, styleSheet.toString());
			} else {
				fail(cssParser.getError());
			}
		}
	}
	
	@Test
	void testAnimationCurveParser() {
		TestUtils.testParser("0.25s ease-in-out-quad", new AnimationCurve(0.25f, Easing.EASE_IN_OUT_QUAD), new AnimationCurveParser());
	}
	
}
