package dev.prozilla.pine.common.system.resource;

public interface Resource {
	
	/**
	 * @return The path to the file this resource was loaded from
	 */
	String getPath();
	
	/**
	 * Removes this resource from the resource pool and deletes it.
	 */
	void destroy();
	
}
