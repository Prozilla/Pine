package dev.prozilla.pine.common.util.collection;

public interface Pipable<T, P extends Pipeline<T>> {
	
	/**
	 * Creates a pipeline with this object and a given object.
	 * @param next The object to add to the pipeline after this object.
	 * @return The new pipeline.
	 */
	P then(T next);
	
}
