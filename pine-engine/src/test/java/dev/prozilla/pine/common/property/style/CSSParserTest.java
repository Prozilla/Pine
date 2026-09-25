package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.style.selector.IdSelector;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CSSParserTest {
	
	@Test
	void testStyleSheetParser() {
		StyleSheet expected = new StyleSheet();
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.COLOR, Color.hex("#fff"));
		expected.addRule(new IdSelector("id"), StyledPropertyKey.COLOR, Color.hex("#000"));
		
		CSSParser cssParser = new CSSParser();
		
		TestUtils.testParser("* { color: #fff } #id { color: #000 }", expected, cssParser);
		TestUtils.testParser("* { COLOR: #FFF } #id { COLOR: #000 }", expected, cssParser);
	}
	
	@Test
	void testStyleSheetParseToString() {
		CSSParser cssParser = new CSSParser();
		String[] inputStrings = new String[] {
			":hover { color: rgb(0.25, 0.75, 0.5); }",
			"* { color: rgb(1.0, 1.0, 1.0); } #id { color: rgb(0.0, 0.0, 0.0); }"
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
	
}
