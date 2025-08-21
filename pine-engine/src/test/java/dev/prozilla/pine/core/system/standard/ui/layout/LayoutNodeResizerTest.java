package dev.prozilla.pine.core.system.standard.ui.layout;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LayoutNodeResizerTest {
	
	@Test
	void testEmptyLayoutNode() {
		LayoutNode layoutNode = new LayoutNode();
		Node node = new Node();
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(), layoutNode.innerSize);
		assertEquals(new Vector2f(), node.currentInnerSize);
		assertEquals(new Vector2f(), node.currentOuterSize);
		assertEquals(0, layoutNode.currentGap);
	}
	
	@Test
	void testEmptyLayoutNodeWithPadding() {
		LayoutNode layoutNode = new LayoutNode();
		Node node = new Node();
		node.padding = new DualDimension(1, 1);
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(), layoutNode.innerSize);
		assertEquals(new Vector2f(2, 2), node.currentInnerSize);
		assertEquals(new Vector2f(2, 2), node.currentOuterSize);
		assertEquals(0, layoutNode.currentGap);
	}
	
	@Test
	void testEmptyLayoutNodeWithPaddingAndMargin() {
		LayoutNode layoutNode = new LayoutNode();
		Node node = new Node();
		node.padding = new DualDimension(1, 1);
		node.margin = new DualDimension(1, 1);
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(), layoutNode.innerSize);
		assertEquals(new Vector2f(2, 2), node.currentInnerSize);
		assertEquals(new Vector2f(4, 4), node.currentOuterSize);
		assertEquals(0, layoutNode.currentGap);
	}
	
	@Test
	void testEmptyLayoutNodeWithGap() {
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.gap = new Dimension(1);
		Node node = new Node();
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(), layoutNode.innerSize);
		assertEquals(new Vector2f(), node.currentInnerSize);
		assertEquals(new Vector2f(), node.currentOuterSize);
		assertEquals(1, layoutNode.currentGap);
	}
	
	@Test
	void testEmptyLayoutNodeWithSize() {
		LayoutNode layoutNode = new LayoutNode();
		Node node = new Node();
		node.size = new DualDimension(1, 1);
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(1, 1), layoutNode.innerSize);
		assertEquals(new Vector2f(1, 1), node.currentInnerSize);
		assertEquals(new Vector2f(1, 1), node.currentOuterSize);
		assertEquals(0, layoutNode.currentGap);
	}
	
	@Test
	void testLayoutNodeWithRemainingWidth() {
		Node childNode1 = new Node();
		childNode1.currentOuterSize.set(1, 1);
		Node childNode2 = new Node();
		childNode2.currentOuterSize.set(1, 1);
		
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.direction = Direction.RIGHT;
		layoutNode.gap = new Dimension(1);
		layoutNode.childNodes.add(childNode1);
		layoutNode.childNodes.add(childNode2);
		Node node = new Node();
		node.size = new DualDimension(4, 1);
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(4, 1), layoutNode.innerSize);
		assertEquals(new Vector2f(4, 1), node.currentInnerSize);
		assertEquals(new Vector2f(4, 1), node.currentOuterSize);
		assertEquals(1, layoutNode.currentGap);
	}
	
	@Test
	void testLayoutNodeWithOverflowX() {
		Node childNode1 = new Node();
		childNode1.currentOuterSize.set(1, 1);
		Node childNode2 = new Node();
		childNode2.currentOuterSize.set(1, 1);
		
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.direction = Direction.RIGHT;
		layoutNode.gap = new Dimension(1);
		layoutNode.childNodes.add(childNode1);
		layoutNode.childNodes.add(childNode2);
		Node node = new Node();
		node.size = new DualDimension(Dimension.auto(), new Dimension(1));
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(3, 1), layoutNode.innerSize);
		assertEquals(new Vector2f(3, 1), node.currentInnerSize);
		assertEquals(new Vector2f(3, 1), node.currentOuterSize);
		assertEquals(1, layoutNode.currentGap);
	}
	
	@Test
	void testLayoutNodeWithOverflowY() {
		Node childNode1 = new Node();
		childNode1.currentOuterSize.set(1, 1);
		Node childNode2 = new Node();
		childNode2.currentOuterSize.set(1, 1);
		
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.direction = Direction.DOWN;
		layoutNode.gap = new Dimension(1);
		layoutNode.childNodes.add(childNode1);
		layoutNode.childNodes.add(childNode2);
		Node node = new Node();
		node.size = new DualDimension(new Dimension(1), Dimension.auto());
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(1, 3), layoutNode.innerSize);
		assertEquals(new Vector2f(1, 3), node.currentInnerSize);
		assertEquals(new Vector2f(1, 3), node.currentOuterSize);
		assertEquals(1, layoutNode.currentGap);
	}
	
	@Test
	void testLayoutNodeWithSpaceBetween() {
		Node childNode1 = new Node();
		childNode1.currentOuterSize.set(1, 1);
		Node childNode2 = new Node();
		childNode2.currentOuterSize.set(1, 1);
		
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.direction = Direction.RIGHT;
		layoutNode.distribution = LayoutNode.Distribution.SPACE_BETWEEN;
		layoutNode.childNodes.add(childNode1);
		layoutNode.childNodes.add(childNode2);
		Node node = new Node();
		node.size = new DualDimension(4, 1);
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(4, 1), layoutNode.innerSize);
		assertEquals(new Vector2f(4, 1), node.currentInnerSize);
		assertEquals(new Vector2f(4, 1), node.currentOuterSize);
		assertEquals(2, layoutNode.currentGap);
	}
	
	@Test
	void testLayoutNodeWithAutoHeight() {
		Node childNode1 = new Node();
		childNode1.currentOuterSize.set(1, 1);
		Node childNode2 = new Node();
		childNode2.currentOuterSize.set(1, 1);
		
		LayoutNode layoutNode = new LayoutNode();
		layoutNode.direction = Direction.RIGHT;
		layoutNode.childNodes.add(childNode1);
		layoutNode.childNodes.add(childNode2);
		Node node = new Node();
		node.size = new DualDimension(new Dimension(4), Dimension.auto());
		
		LayoutNodeResizer.resizeCanvasGroup(layoutNode, node);
		
		assertEquals(new Vector2f(4, 1), layoutNode.innerSize);
		assertEquals(new Vector2f(4, 1), node.currentInnerSize);
		assertEquals(new Vector2f(4, 1), node.currentOuterSize);
		assertEquals(0, layoutNode.currentGap);
	}
	
}
