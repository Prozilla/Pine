package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NodeTest {
	
	@Test
	void testQueryWithTypeSelector() {
		Node node = new Node();
		node.htmlTag = Node.PARAGRAPH_TAG;
		
		assertNotNull(node.querySelector(Selector.UNIVERSAL));
		assertNotNull(node.querySelector(Node.PARAGRAPH_TAG));
		assertNull(node.querySelector(Node.DIV_TAG));
	}
	
	@Test
	void testQueryWithChildSelector() {
		Node parent = new Node();
		parent.htmlTag = Node.DIV_TAG;
		
		Node child = new Node();
		child.htmlTag = Node.PARAGRAPH_TAG;
		parent.children.add(child);
		
		assertEquals(parent, parent.querySelector(Selector.UNIVERSAL));
		assertEquals(child, parent.querySelector(Node.PARAGRAPH_TAG));
		assertEquals(parent, parent.querySelector(Node.DIV_TAG));
		assertNull(parent.querySelector(Node.HEADING_1_TAG));
	}
	
}
