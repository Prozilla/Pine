package property;

import dev.prozilla.pine.common.math.easing.Easing;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.property.style.selector.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestLoggingExtension;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StyledPropertyTest {
	
	@Test
	void testSelectorParse() {
		TestUtils.testParse("div.class#id:not(:hover)", new SelectorCombo(new TypeSelector("div"), new ClassSelector("class"), new IdSelector("id"), new NotSelector(new ModifierSelector("hover"))), Selector::parse);
		TestUtils.testParse("*", Selector.UNIVERSAL, Selector::parse);
	}
	
	@Test
	void testStyleSheetParse() {
		String input = ":hover { color: rgba(0.25, 0.75, 0.5, 1.0); }";
		StyleSheet styleSheet = StyleSheet.parse(input);
		
		assertEquals(input, styleSheet.toString());
	}
	
	@Test
	void testAnimationCurveParse() {
		TestUtils.testParse("0.25s ease-in-out-quad", new AnimationCurve(0.25f, Easing.EASE_IN_OUT_QUAD), AnimationCurve::parse);
	}
	
}
