package dev.prozilla.pine.common.util;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {
	
	private ListUtils() {}
	
	public static <E> List<E> createSingleton(E element) {
		List<E> list = new ArrayList<>();
		list.add(element);
		return list;
	}
	
}
