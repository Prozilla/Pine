package dev.prozilla.pine.common.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Destructible {
	
	/**
	 * Destroys this object.
	 */
	void destroy();
	
	/**
	 * Destroys all objects in a collection and clears the collection.
	 * This method can also be used for objects that remove themselves from the collection during their destruction process,
	 * as it will clone and clear the original collection before iterating over it.
	 * @param destructibles The objects to destroy
	 * @param <D> The type of objects in the collection
	 */
	static <D extends Destructible> void destroyAll(Collection<D> destructibles) {
		if (destructibles.isEmpty()) {
			return;
		}
		
		List<D> clone = new ArrayList<>(destructibles);
		destructibles.clear();
		for (D destructible : clone) {
			if (destructible != null) {
				destructible.destroy();
			}
		}
	}
	
}
