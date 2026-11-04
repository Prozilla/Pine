package dev.prozilla.pine.common;

import dev.prozilla.pine.common.lifecycle.Destructible;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A destructible object with a holder (reference) counter that gets destroyed when there are no holders (references).
 * <p>
 *     Other objects can use {@link #hold()} when they store a reference to this object and {@link #drop()} when they no longer do.
 *     When this object has no holders, calling {@link #checkHoldState()} will destroy it.
 * </p>
 */
public interface Holdable extends Destructible {
	
	/**
	 * Increases the holder count of this object.
	 * <p>
	 *     Called by other objects to indicate that they are storing a reference to this object.
	 * </p>
	 */
	void hold();
	
	/**
	 * Decreases the holder count of this object.
	 * <p>
	 *     Called by other objects to indicate that they are no longer storing a reference to this object.
	 * </p>
	 */
	void drop();
	
	/**
	 * Checks if this object is being held by at least one other object and destroys it if it isn't.
	 * @see #isHeld()
	 * @see #destroy()
	 * @return {@code false} if the object was not being held and has been destroyed.
	 */
	default boolean checkHoldState() {
		if (!isHeld()) {
			destroy();
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this object is being held by at least one other object.
	 * @return {@code true} if this object is being held.
	 */
	default boolean isHeld() {
		return getHolderCount() > 0;
	}
	
	/**
	 * Returns the amount of objects holding this object.
	 * @return The amount of objects holding this object.
	 */
	int getHolderCount();
	
	static <H extends Holdable> H hold(H holdable) {
		if (holdable != null) {
			holdable.hold();
		}
		return holdable;
	}
	
	static void drop(Holdable... holdables) {
		for (Holdable holdable : holdables) {
			if (holdable != null) {
				holdable.drop();
			}
		}
	}
	
	@Contract("_ -> null")
	static <H extends Holdable> H drop(H holdable) {
		if (holdable != null) {
			holdable.drop();
		}
		return null;
	}
	
	static <H extends Holdable> H swap(H oldHoldable, H newHoldable) {
		if (oldHoldable != newHoldable) {
			if (newHoldable != null) {
				newHoldable.hold();
			}
			if (oldHoldable != null) {
				oldHoldable.drop();
			}
		}
		
		return newHoldable;
	}
	
	static <H extends Holdable> void dropAndClear(Collection<H> holdables) {
		if (holdables.isEmpty()) {
			return;
		}
		
		List<H> clone = new ArrayList<>(holdables);
		holdables.clear();
		for (H holdable : clone) {
			if (holdable != null) {
				holdable.drop();
			}
		}
	}
	
}
