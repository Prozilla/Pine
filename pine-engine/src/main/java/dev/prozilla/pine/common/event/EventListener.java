package dev.prozilla.pine.common.event;

@FunctionalInterface
public interface EventListener<E> {
	
	void handle(E event);
	
}
