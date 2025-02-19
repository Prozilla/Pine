package ansi;

import dev.prozilla.pine.common.system.Ansi;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import util.TestExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnsiTest {
	
	@Test
	void testStrip() {
		String original = "This is a piece of text.";
		String decorated = Ansi.black(Ansi.bold(original)).replace("piece", Ansi.green("piece"));
		
		assertEquals(Ansi.strip(decorated), original, "Stripped text should not contain any ANSI escape codes.");
	}
	
}
