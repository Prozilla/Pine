package dev.prozilla.pine.core.component.ui.style;

public enum BorderStyle {
	NONE("none"),
	SOLID("solid");
	
	private final String string;
	
	BorderStyle(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
