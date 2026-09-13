package dev.prozilla.pine.core.component.ui.style;

public enum LineStyle {
	NONE("none"),
	SOLID("solid");
	
	private final String string;
	
	LineStyle(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
