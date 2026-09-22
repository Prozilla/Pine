package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.test.TestLoggingExtension;
import dev.prozilla.pine.test.TestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HTMLParserTest {
	
	@Test
	void testBlank() {
		HTMLParser parser = new HTMLParser();
		TestUtils.testParserFailure("", parser);
		TestUtils.testParserFailure(" ", parser);
	}
	
	@Test
	void testEmptyDiv() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<div></div>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of empty div should have result");
		assertEquals(Node.DIV_TAG, result.htmlTag, "result should have the correct html tag");
		assertTrue(result.useDefaultStyleSheet, "result uses default style sheet");
	}
	
	@Test
	void testNestedDiv() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<div><div></div></div>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of nested div should have result");
		assertEquals(Node.DIV_TAG, result.htmlTag, "result should have the correct html tag");
		assertEquals(1, result.getChildren().size(), "result should have one child");
		assertEquals(Node.DIV_TAG, ((NodePrefab)result.getChildren().getFirst()).htmlTag, "child of result should have the correct html tag");
	}
	
	@Test
	void testEmptyButton() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<button></button>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of empty button should have result");
		assertEquals(Node.BUTTON_TAG, result.htmlTag, "result should have the correct html tag");
	}
	
	@Test
	void testSelfClosingButton() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<button/>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of self-closing button should have result");
		assertEquals(Node.BUTTON_TAG, result.htmlTag, "result should have the correct html tag");
	}
	
	@Test
	void testSelfClosingButtonWithAttribute() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<button data-attribute=\"value\"/>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of self-closing button with attribute should have result");
		assertEquals(Node.BUTTON_TAG, result.htmlTag, "result should have the correct html tag");
		assertEquals(1, result.attributes.size(), "result should have one attribute");
		assertEquals("value", result.attributes.get("data-attribute"), "result should have the correct attribute");
	}
	
	@Test
	void testSelfClosingButtonWithAttributes() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<button data-attribute-1=\"foo\" data-attribute-2=\"bar\"/>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of self-closing button with attribute should have result");
		assertEquals(Node.BUTTON_TAG, result.htmlTag, "result should have the correct html tag");
		assertEquals(2, result.attributes.size(), "result should have one attribute");
		assertEquals("foo", result.attributes.get("data-attribute-1"), "result should have the correct first attribute");
		assertEquals("bar", result.attributes.get("data-attribute-2"), "result should have the correct second attribute");
	}
	
	@Test
	void testTitleWithCharacterReference() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<title>Hello&nbsp;world</title>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of title with character reference should have result");
		assertEquals("Hello world", parser.getTitle(), "title should parse correctly");
	}
	
	@Test
	void testParagraph() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<p>FooBar</p>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of paragraph should have result");
		assertEquals(Node.PARAGRAPH_TAG, result.htmlTag, "result should have the correct html tag");
		assertInstanceOf(TextPrefab.class, result);
		
		TextPrefab textPrefab = (TextPrefab)result;
		assertEquals("FooBar", textPrefab.text, "result should have the correct text content");
	}
	
	@Test
	void testDivWithParagraph() {
		HTMLParser parser = new HTMLParser();
		if (!parser.parse("<div><p>FooBar</p></div>")) {
			fail(parser.getError());
		}
		
		NodePrefab result = parser.getResult();
		assertNotNull(result, "parsing of div with paragraph should have result");
		assertEquals(Node.DIV_TAG, result.htmlTag, "result should have the correct html tag");
		
		Prefab childPrefab = result.getChildren().getFirst();
		assertInstanceOf(TextPrefab.class, childPrefab);
		TextPrefab textPrefab = (TextPrefab)childPrefab;
		assertEquals("FooBar", textPrefab.text, "result should have the correct text content");
	}

}
