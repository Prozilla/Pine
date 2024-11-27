package dev.prozilla.pine.core.state.input;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for keyboard keys.
 * See: <a href="https://www.glfw.org/docs/3.3/group__keys.html">GLFW: Keyboard key tokens</a>
 */
public enum Key {
	
	SPACE(GLFW_KEY_SPACE),
	ESCAPE(GLFW_KEY_ESCAPE),
	ENTER(GLFW_KEY_ENTER),
	TAB(GLFW_KEY_TAB),
	
	// Modifiers
	L_SHIFT(GLFW_KEY_LEFT_SHIFT),
	L_CONTROL(GLFW_KEY_LEFT_CONTROL),
	L_ALT(GLFW_KEY_LEFT_ALT),
	R_SHIFT(GLFW_KEY_RIGHT_SHIFT),
	R_CONTROL(GLFW_KEY_RIGHT_CONTROL),
	R_ALT(GLFW_KEY_RIGHT_ALT),
	
	// Arrows
	LEFT_ARROW(GLFW_KEY_LEFT),
	RIGHT_ARROW(GLFW_KEY_RIGHT),
	UP_ARROW(GLFW_KEY_UP),
	DOWN_ARROW(GLFW_KEY_DOWN),
	
	// Letters
	A(GLFW_KEY_A),
	B(GLFW_KEY_B),
	C(GLFW_KEY_C),
	D(GLFW_KEY_D),
	E(GLFW_KEY_E),
	F(GLFW_KEY_F),
	G(GLFW_KEY_G),
	H(GLFW_KEY_H),
	I(GLFW_KEY_I),
	J(GLFW_KEY_J),
	K(GLFW_KEY_K),
	L(GLFW_KEY_L),
	M(GLFW_KEY_M),
	N(GLFW_KEY_N),
	O(GLFW_KEY_O),
	P(GLFW_KEY_P),
	Q(GLFW_KEY_Q),
	R(GLFW_KEY_R),
	S(GLFW_KEY_S),
	T(GLFW_KEY_T),
	U(GLFW_KEY_U),
	V(GLFW_KEY_V),
	W(GLFW_KEY_W),
	X(GLFW_KEY_X),
	Y(GLFW_KEY_Y),
	Z(GLFW_KEY_Z),
	
	// Numbers
	NUM_0(GLFW_KEY_0),
	NUM_1(GLFW_KEY_1),
	NUM_2(GLFW_KEY_2),
	NUM_3(GLFW_KEY_3),
	NUM_4(GLFW_KEY_4),
	NUM_5(GLFW_KEY_5),
	NUM_6(GLFW_KEY_6),
	NUM_7(GLFW_KEY_7),
	NUM_8(GLFW_KEY_8),
	NUM_9(GLFW_KEY_9),
	
	// Function keys
	F1(GLFW_KEY_F1),
	F2(GLFW_KEY_F2),
	F3(GLFW_KEY_F3),
	F4(GLFW_KEY_F4),
	F5(GLFW_KEY_F5),
	F6(GLFW_KEY_F6),
	F7(GLFW_KEY_F7),
	F8(GLFW_KEY_F8),
	F9(GLFW_KEY_F9),
	F10(GLFW_KEY_F10),
	F11(GLFW_KEY_F11),
	F12(GLFW_KEY_F12);
	
	private final int value;
	
	Key(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW key.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		boolean valid = false;
		for (Key key : values()) {
			if (key.value == value) {
				valid = true;
				break;
			}
		}
		return valid;
	}
}
