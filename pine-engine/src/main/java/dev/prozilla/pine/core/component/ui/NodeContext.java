package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.ContextOf;

@ContextOf(Node.class)
public interface NodeContext {
	
	float getX();
	
	float getY();
	
	float getWidth();
	
	float getHeight();
	
}
