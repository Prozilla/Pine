package dev.prozilla.pine.core.entity;

public interface EntityFinder {
	
	Entity getChildWithTag(String tag);
	
	Entity getParentWithTag(String tag);
	
}
