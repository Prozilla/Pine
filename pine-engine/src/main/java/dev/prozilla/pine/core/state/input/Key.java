package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Mappings for GLFW integer values for keyboard keys based on the US keyboard layout.
 * See: <a href="https://www.glfw.org/docs/3.3/group__keys.html">GLFW: Keyboard key tokens</a>
 */
public enum Key implements IntEnum {
	
	// General
	SPACE(GLFW_KEY_SPACE, "Space"),
	ESCAPE(GLFW_KEY_ESCAPE, "Escape"),
	ENTER(GLFW_KEY_ENTER, "Enter"),
	TAB(GLFW_KEY_TAB, "Tab"),
	CAPS_LOCK(GLFW_KEY_CAPS_LOCK, "Caps Lock"),
	SCROLL_LOCK(GLFW_KEY_SCROLL_LOCK, "Scroll Lock"),
	NUM_LOCK(GLFW_KEY_NUM_LOCK, "Num Lock"),
	BACKSPACE(GLFW_KEY_BACKSPACE, "Backspace"),
	
	// Modifiers
	L_SHIFT(GLFW_KEY_LEFT_SHIFT, "Left Shift"),
	L_CONTROL(GLFW_KEY_LEFT_CONTROL, "Left Control"),
	L_ALT(GLFW_KEY_LEFT_ALT, "Left Alt"),
	L_SUPER(GLFW_KEY_LEFT_SUPER, "Left Super"),
	R_SHIFT(GLFW_KEY_RIGHT_SHIFT, "Right Shift"),
	R_CONTROL(GLFW_KEY_RIGHT_CONTROL, "Right Control"),
	R_ALT(GLFW_KEY_RIGHT_ALT, "Right Alt"),
	R_SUPER(GLFW_KEY_RIGHT_SUPER, "Right Super"),
	
	// Letter keys
	A(GLFW_KEY_A, "A"),
	B(GLFW_KEY_B, "B"),
	C(GLFW_KEY_C, "C"),
	D(GLFW_KEY_D, "D"),
	E(GLFW_KEY_E, "E"),
	F(GLFW_KEY_F, "F"),
	G(GLFW_KEY_G, "G"),
	H(GLFW_KEY_H, "H"),
	I(GLFW_KEY_I, "I"),
	J(GLFW_KEY_J, "J"),
	K(GLFW_KEY_K, "K"),
	L(GLFW_KEY_L, "L"),
	M(GLFW_KEY_M, "M"),
	N(GLFW_KEY_N, "N"),
	O(GLFW_KEY_O, "O"),
	P(GLFW_KEY_P, "P"),
	Q(GLFW_KEY_Q, "Q"),
	R(GLFW_KEY_R, "R"),
	S(GLFW_KEY_S, "S"),
	T(GLFW_KEY_T, "T"),
	U(GLFW_KEY_U, "U"),
	V(GLFW_KEY_V, "V"),
	W(GLFW_KEY_W, "W"),
	X(GLFW_KEY_X, "X"),
	Y(GLFW_KEY_Y, "Y"),
	Z(GLFW_KEY_Z, "Z"),
	
	// Number keys
	NUM_0(GLFW_KEY_0, "0"),
	NUM_1(GLFW_KEY_1, "1"),
	NUM_2(GLFW_KEY_2, "2"),
	NUM_3(GLFW_KEY_3, "3"),
	NUM_4(GLFW_KEY_4, "4"),
	NUM_5(GLFW_KEY_5, "5"),
	NUM_6(GLFW_KEY_6, "6"),
	NUM_7(GLFW_KEY_7, "7"),
	NUM_8(GLFW_KEY_8, "8"),
	NUM_9(GLFW_KEY_9, "9"),
	
	// Numpad
	NUMPAD_0(GLFW_KEY_KP_0, "Numpad 0"),
	NUMPAD_1(GLFW_KEY_KP_1, "Numpad 1"),
	NUMPAD_2(GLFW_KEY_KP_2, "Numpad 2"),
	NUMPAD_3(GLFW_KEY_KP_3, "Numpad 3"),
	NUMPAD_4(GLFW_KEY_KP_4, "Numpad 4"),
	NUMPAD_5(GLFW_KEY_KP_5, "Numpad 5"),
	NUMPAD_6(GLFW_KEY_KP_6, "Numpad 6"),
	NUMPAD_7(GLFW_KEY_KP_7, "Numpad 7"),
	NUMPAD_8(GLFW_KEY_KP_8, "Numpad 8"),
	NUMPAD_9(GLFW_KEY_KP_9, "Numpad 9"),
	NUMPAD_DECIMAL(GLFW_KEY_KP_DECIMAL, "Numpad Decimal"),
	NUMPAD_DIVIDE(GLFW_KEY_KP_DIVIDE, "Numpad Divide"),
	NUMPAD_MULTIPLY(GLFW_KEY_KP_MULTIPLY, "Numpad Multiply"),
	NUMPAD_SUBTRACT(GLFW_KEY_KP_SUBTRACT, "Numpad Subtract"),
	NUMPAD_ADD(GLFW_KEY_KP_ADD, "Numpad Add"),
	NUMPAD_ENTER(GLFW_KEY_KP_ENTER, "Numpad Enter"),
	NUMPAD_EQUAL(GLFW_KEY_KP_EQUAL, "Numpad Equal"),
	
	// Function keys
	F1(GLFW_KEY_F1, "F1"),
	F2(GLFW_KEY_F2, "F2"),
	F3(GLFW_KEY_F3, "F3"),
	F4(GLFW_KEY_F4, "F4"),
	F5(GLFW_KEY_F5, "F5"),
	F6(GLFW_KEY_F6, "F6"),
	F7(GLFW_KEY_F7, "F7"),
	F8(GLFW_KEY_F8, "F8"),
	F9(GLFW_KEY_F9, "F9"),
	F10(GLFW_KEY_F10, "F10"),
	F11(GLFW_KEY_F11, "F11"),
	F12(GLFW_KEY_F12, "F12"),
	F13(GLFW_KEY_F13, "F13"),
	F14(GLFW_KEY_F14, "F14"),
	F15(GLFW_KEY_F15, "F15"),
	F16(GLFW_KEY_F16, "F16"),
	F17(GLFW_KEY_F17, "F17"),
	F18(GLFW_KEY_F18, "F18"),
	F19(GLFW_KEY_F19, "F19"),
	F20(GLFW_KEY_F20, "F20"),
	F21(GLFW_KEY_F21, "F21"),
	F22(GLFW_KEY_F22, "F22"),
	F23(GLFW_KEY_F23, "F23"),
	F24(GLFW_KEY_F24, "F24"),
	F25(GLFW_KEY_F25, "F25"),
	
	// Cursor control keys
	DELETE(GLFW_KEY_DELETE, "Delete"),
	INSERT(GLFW_KEY_INSERT, "Insert"),
	PAGE_UP(GLFW_KEY_PAGE_UP, "Page Up"),
	PAGE_DOWN(GLFW_KEY_PAGE_DOWN, "Page Down"),
	HOME(GLFW_KEY_HOME, "Home"),
	END(GLFW_KEY_END, "End"),
	
	// Arrow keys
	LEFT_ARROW(GLFW_KEY_LEFT, "Left Arrow"),
	RIGHT_ARROW(GLFW_KEY_RIGHT, "Right Arrow"),
	UP_ARROW(GLFW_KEY_UP, "Up Arrow"),
	DOWN_ARROW(GLFW_KEY_DOWN, "Down Arrow"),
	
	// Other
	MENU(GLFW_KEY_MENU, "Menu"),
	PAUSE(GLFW_KEY_PAUSE, "Pause"),
	PRINT_SCREEN(GLFW_KEY_PRINT_SCREEN, "Print Screen"),
	APOSTROPHE(GLFW_KEY_APOSTROPHE, "'"),
	COMMA(GLFW_KEY_COMMA, ","),
	MINUS(GLFW_KEY_MINUS, "-"),
	PERIOD(GLFW_KEY_PERIOD, "."),
	SLASH(GLFW_KEY_SLASH, "/"),
	SEMICOLON(GLFW_KEY_SEMICOLON, ";"),
	EQUAL(GLFW_KEY_EQUAL, "="),
	L_BRACKET(GLFW_KEY_LEFT_BRACKET, "["),
	R_BRACKET(GLFW_KEY_RIGHT_BRACKET, "]"),
	GRAVE_ACCENT(GLFW_KEY_GRAVE_ACCENT, "`"),
	BACKSLASH(GLFW_KEY_BACKSLASH, "\\"),
	WORLD_1(GLFW_KEY_WORLD_1, "Foreign 1"),
	WORLD_2(GLFW_KEY_WORLD_2, "Foreign 2");
	
	private final int value;
	/** String based on US keyboard layout */
	private final String usString;
	
	Key(int value, String usString) {
		this.value = value;
		this.usString = usString;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return usString;
	}
	
	/**
	 * Checks if a given value is a valid value for a GLFW key.
	 * @param value Value to test
	 * @return True if the value is a valid value
	 */
	public static boolean isValid(int value) {
		return EnumUtils.hasIntValue(values(), value);
	}
	
}
