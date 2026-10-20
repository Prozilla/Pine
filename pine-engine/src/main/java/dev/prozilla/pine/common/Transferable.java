package dev.prozilla.pine.common;

/**
 * An object whose data can be transferred between objects.
 *
 * <p>
 *     The data can be moved to another object (e.g., using {@link #transmit(Transceivable)}) or to a new object (e.g., using {@link #clone()}).
 * </p>
 */
public interface Transferable<T extends Transferable<T>> extends Transceivable<T>, Cloneable<T> {

}
