package dev.prozilla.pine.core.state.input;

public enum ModifierKey {
	/** Left and right shift key. */
	SHIFT(new Key[]{Key.L_SHIFT, Key.R_SHIFT}),
	/** Left and right control key. */
	CONTROL(new Key[]{Key.L_CONTROL, Key.R_CONTROL}),
	/** Left and right alt key. */
	ALT(new Key[]{Key.L_ALT, Key.R_ALT}),
	/** Left and right super key. */
	SUPER(new Key[]{Key.L_SUPER, Key.R_SUPER});
	
	private final Key[] keys;
	
	ModifierKey(Key[] keys) {
		this.keys = keys;
	}
	
	public Key[] getKeys() {
		return keys;
	}
	
}
