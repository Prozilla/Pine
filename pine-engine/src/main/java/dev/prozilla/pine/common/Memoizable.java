package dev.prozilla.pine.common;

public interface Memoizable {
	
	void markAsDirty();
	
	boolean isDirty();
	
}
