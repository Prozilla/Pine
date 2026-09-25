package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SelectorParserTest {
	
	@Test
	void testSelectorParser() {
		SelectorParser selectorParser = new SelectorParser();
		TestUtils.testParser("div.class#id:not(:hover)", new CompoundSelector(new TypeSelector("div"), new ClassSelector("class"), new IdSelector("id"), new NotSelector(new ModifierSelector("hover"))), selectorParser);
		TestUtils.testParser("*", Selector.UNIVERSAL, selectorParser);
		TestUtils.testParser("div > p", new ChildSelector(new TypeSelector("div"), new TypeSelector("p")), selectorParser);
		TestUtils.testParser("div, p", new SelectorList(new TypeSelector("p"), new TypeSelector("div")), selectorParser);
	}
	
	@Test
	void testSelectorParserFailure() {
		SelectorParser selectorParser = new SelectorParser();
		TestUtils.testParserFailure(":", "Invalid modifier", selectorParser);
		TestUtils.testParserFailure(".", "Invalid class", selectorParser);
		TestUtils.testParserFailure("#", "Invalid id", selectorParser);
	}
	
}
