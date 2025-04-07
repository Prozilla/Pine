package dev.prozilla.pine.common.property.style;

public enum StyledPropertyName {
	COLOR("color"),
	SIZE("size");
	
	private final String string;
	
	StyledPropertyName(String string) {
		this.string = string;
	}
	
	public String toString() {
		return string;
	}
}
