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
	 * Destroys the destructible and returns {@code null}.
	 *
	 * <p>
	 *     Example usage:
	 *     <pre>
	 *         {@code
	 *         Entity entity = world.addEntity(prefab);
	 *
	 *         // Then, in some method that might get called repeatedly
	 *         entity = Destructible.destroy(entity);
	 *
	 *         // Which is the equivalent of this:
	 *         if (entity != null) {
	 *             entity.destroy();
	 *             entity = null;
	 *         }
	 *         }
	 *     </pre>
	 * </p>
	 * @param destructible The destructible to destroy or {@code null}
	 * @return {@code null}
	 * @param <D> The type of destructible
	 */
	static <D extends Destructible> D destroy(D destructible) {
		if (destructible != null) {
			destructible.destroy();
		}
		return null;
	}
	
	/**
	 * Destroys all objects in a collection and clears the collection.
	 * This method can also be used for objects that remove themselves from the collection during their destruction process,
	 * as it will clone and clear the original collection before iterating over it.
	 * @param destructibles The objects to destroy
	 * @param <D> The type of objects in the collection
	 */
	static <D extends Destructible> void destroyAndClear(Collection<D> destructibles) {
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
	
	/**
	 * Destroys all objects in a collection.
	 *
	 * <p>Use this method if the destroy methods of the items depend on being able to remove themselves from the collection.
	 * Which would cause a concurrent modification exception if done using a simple for-each loop.</p>
	 * @param destructibles The objects to destroy
	 * @param <D> The type of objects in the collection
	 */
	static <D extends Destructible> void destroyAll(Collection<D> destructibles) {
		if (destructibles.isEmpty()) {
			return;
		}
		
		List<D> clone = new ArrayList<>(destructibles);
		for (D destructible : clone) {
			if (destructible != null) {
				destructible.destroy();
			}
		}
	}
	
	
}
