package dev.prozilla.pine.common.util;

import java.util.Queue;
import java.util.function.Consumer;

/**
 * Utility methods related to queues.
 */
public final class QueueUtils {
	
	private QueueUtils() {}
	
	public static <E> void drain(Queue<E> queue, Consumer<E> consumer) {
		E element;
		while ((element = queue.poll()) != null) {
			consumer.accept(element);
		}
	}
	
}
