package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.asset.style.CSSParser;
import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.common.asset.style.StyledPropertyKey;
import dev.prozilla.pine.common.asset.style.selector.IdSelector;
import dev.prozilla.pine.common.asset.style.selector.Selector;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.ui.style.LineStyle;
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
	void testCSSBorderShorthandParsing() {
		StyleSheet expected = new StyleSheet();
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.BORDER_WIDTH, new Dimension(1));
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.BORDER_STYLE, LineStyle.SOLID);
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.BORDER_COLOR, Color.black());
		
		CSSParser cssParser = new CSSParser();
		
		TestUtils.testParser("* { border-width: 1px; border-style: solid; border-color: black; }", expected, cssParser);
		TestUtils.testParser("* { border: 1px solid black; }", expected, cssParser);
	}
	
	@Test
	void testCSSFontShorthandParsing() {
		StyleSheet expected = new StyleSheet();
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.FONT_SIZE, new Dimension(16));
		expected.addRule(Selector.UNIVERSAL, StyledPropertyKey.FONT_FAMILY, "sans-serif");
		
		CSSParser cssParser = new CSSParser();
		
		TestUtils.testParser("* { font-size: 16px; font-family: sans-serif; }", expected, cssParser);
		TestUtils.testParser("* { font: 16px sans-serif; }", expected, cssParser);
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
