package dev.prozilla.pine.common.util;

public class EnumUtils {
	
	public static <E extends Enum<E>> E findByName(E[] values, String name) {
		for (E element : values) {
			if (element.toString().equals(name)) {
				return element;
			}
		}
		
		return null;
	}
	
}
