package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.Transceivable;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an RGBA color.
 */
public class Color implements Printable, Cloneable<Color>, Transceivable<Color> {
	
	/** This value specifies the red component. */
	private float red;
	
	/** This value specifies the green component. */
	private float green;
	
	/** This value specifies the blue component. */
	private float blue;
	
	/** This value specifies the transparency. */
	private float alpha;
	
	/** The default color is black. */
	public Color() {
		this(0f, 0f, 0f);
	}
	
	/**
	 * Creates an RGB-Color from a java.awt.Color instance.
	 * @param color Color instance
	 * @deprecated Since 2.1.0
	 */
	@Deprecated
	public Color(java.awt.Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	/**
	 * Creates an RGB-Color with an alpha value of 1.
	 * @param red The red component. Range from 0f to 1f.
	 * @param green The green component. Range from 0f to 1f.
	 * @param blue The blue component. Range from 0f to 1f.
	 */
	public Color(float red, float green, float blue) {
		this(red, green, blue, 1f);
	}
	
	/**
	 * Creates an RGBA-Color.
	 * @param red The red component. Range from 0f to 1f.
	 * @param green The green component. Range from 0f to 1f.
	 * @param blue The blue component. Range from 0f to 1f.
	 * @param alpha The transparency. Range from 0f to 1f.
	 */
	public Color(float red, float green, float blue, float alpha) {
		setRGB(red, green, blue);
		setAlpha(alpha);
	}
	
	/**
	 * Creates an RGB-Color with an alpha value of 1.
	 * @param red The red component. Range from 0 to 255.
	 * @param green The green component. Range from 0 to 255.
	 * @param blue The blue component. Range from 0 to 255.
	 */
	public Color(int red, int green, int blue) {
		this(red, green, blue, 255);
	}
	
	/**
	 * Creates an RGBA-Color.
	 * @param red The red component. Range from 0 to 255.
	 * @param green The green component. Range from 0 to 255.
	 * @param blue The blue component. Range from 0 to 255.
	 * @param alpha The transparency. Range from 0 to 255.
	 */
	public Color(int red, int green, int blue, int alpha) {
		setRGB(red, green, blue);
		setAlpha(alpha);
	}
	
	@Override
	public void transmit(Color target) {
		Checks.isNotNull(target, "target");
		target.set(getRed(), getGreen(), getBlue(), getAlpha());
	}
	
	public Color setRGB(Color color) {
		setRGB(color.getRed(), color.getGreen(), color.getBlue());
		return this;
	}
	
	public Color set(int red, int green, int blue, int alpha) {
		setRGB(red, green, blue);
		setAlpha(alpha);
		return this;
	}
	
	public Color set(float red, float green, float blue, float alpha) {
		setRGB(red, green, blue);
		setAlpha(alpha);
		return this;
	}
	
	public Color setRGB(int red, int green, int blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
		return this;
	}
	
	public Color setRGB(float red, float green, float blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
		return this;
	}
	
	/**
	 * Returns the red component. Range from 0f to 1f.
	 * @return The red component.
	 */
	public float getRed() {
		return red;
	}
	
	/**
	 * Sets the red component.
	 * @param red The red component. Range from 0f to 1f.
	 */
	public Color setRed(float red) {
		this.red = MathUtils.clamp(red, 0f, 1f);
		return this;
	}
	
	/**
	 * Sets the red component.
	 * @param red The red component. Range from 0 to 255.
	 */
	public Color setRed(int red) {
		return setRed(red / 255f);
	}
	
	/**
	 * Returns the green component. Range from 0f to 1f.
	 * @return The green component.
	 */
	public float getGreen() {
		return green;
	}
	
	/**
	 * Sets the green component.
	 * @param green The green component. Range from 0f to 1f.
	 */
	public Color setGreen(float green) {
		this.green = MathUtils.clamp(green, 0f, 1f);
		return this;
	}
	
	/**
	 * Sets the green component.
	 * @param green The green component. Range from 0 to 255.
	 */
	public Color setGreen(int green) {
		return setGreen(green / 255f);
	}
	
	/**
	 * Returns the blue component. Range from 0f to 1f.
	 * @return The blue component.
	 */
	public float getBlue() {
		return blue;
	}
	
	/**
	 * Sets the blue component.
	 * @param blue The blue component. Range from 0f to 1f.
	 */
	public Color setBlue(float blue) {
		this.blue = MathUtils.clamp(blue, 0f, 1f);
		return this;
	}
	
	/**
	 * Sets the blue component.
	 * @param blue The blue component. Range from 0 to 255.
	 */
	public Color setBlue(int blue) {
		return setBlue(blue / 255f);
	}
	
	/**
	 * Returns the transparency. Range from 0f to 1f.
	 * @return The transparency.
	 */
	public float getAlpha() {
		return alpha;
	}
	
	/**
	 * Sets the transparency.
	 * @param alpha The transparency. Range from 0f to 1f.
	 */
	public Color setAlpha(float alpha) {
		this.alpha = MathUtils.clamp(alpha, 0f, 1f);
		return this;
	}
	
	/**
	 * Sets the transparency.
	 * @param alpha The transparency. Range from 0 to 255.
	 */
	public Color setAlpha(int alpha) {
		return setAlpha(alpha / 255f);
	}
	
	public Color multiply(float scalar) {
		setRGB(red * scalar, green * scalar, blue * scalar);
		setAlpha(alpha * scalar);
		return this;
	}
	
	/**
	 * Mixes half of this color with half of another color.
	 * @param color Color to mix with this color
	 */
	public Color mix(Color color) {
		return mix(color, 0.5f);
	}
	
	/**
	 * Mixes this color with another color based on a factor.
	 * @param color Color to mix with this color
	 * @param factor Mixing factor, Range from 0f to 1f.
	 */
	public Color mix(Color color, float factor) {
		Checks.isNotNull(color, "color");
		float antiFactor = 1 - factor;
		
		setRed(red * antiFactor + color.getRed() * factor);
		setGreen(green * antiFactor + color.getGreen() * factor);
		setBlue(blue * antiFactor + color.getBlue() * factor);
		setAlpha(alpha * antiFactor + color.getAlpha() * factor);
		
		return this;
	}
	
	/**
	 * Returns the color as a (x,y,z)-Vector.
	 * @return The color as vec3.
	 */
	@Contract("-> new")
	public Vector3f toVector3f() {
		return new Vector3f(red, green, blue);
	}
	
	/**
	 * Returns the color as a (x,y,z,w)-Vector.
	 * @return The color as vec4.
	 */
	@Contract("-> new")
	public Vector4f toVector4f() {
		return new Vector4f(red, green, blue, alpha);
	}
	
	@Contract("-> new")
	public Colour toColour() {
		return new Colour(red, green, blue, alpha);
	}
	
	@Override
	public Color self() {
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return this == object || (object instanceof Color color && equals(color));
	}
	
	/**
	 * Checks if two colors are equal.
	 * Two colors are equal if they share the same R, G, B and A values.
	 * @param color Color to compare with
	 */
	@Override
	public boolean equals(Color color) {
		return color != null && color.getRed() == red && color.getGreen() == green && color.getBlue() == blue && color.getAlpha() == alpha;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(red, green, blue, alpha);
	}
	
	@Override
	public Color clone() {
		return new Color(red, green, blue, alpha);
	}
	
	@Override
	public @NotNull String toString() {
		if (alpha != 1) {
			return String.format("rgba(%s, %s, %s, %s)", red, green, blue, alpha);
		} else {
			return String.format("rgb(%s, %s, %s)", red, green, blue);
		}
	}
	
	public static Color hsl(int hue, int saturation, int lightness) {
		return hsl(hue / 255f, saturation / 255f, lightness / 255f);
	}
	
	/**
	 * Source: <a href="https://stackoverflow.com/a/9493060/17069783">HSL to RGB color conversion</a>
	 */
	public static Color hsl(float hue, float saturation, float lightness) {
		float red, green, blue;
		
		if (saturation == 0) {
			red = green = blue = lightness;
		} else {
			float q = lightness < 0.5f ? lightness * (1f + saturation) : lightness + saturation - lightness * saturation;
			float p = 2f * lightness - q;
			red = hueToRGB(p, q, hue + 1f/3);
			green = hueToRGB(p, q, hue);
			blue = hueToRGB(p, q, hue - 1f/3);
		}
		
		return new Color(red, green, blue);
	}
	
	/**
	 * Source: <a href="https://stackoverflow.com/a/9493060/17069783">HSL to RGB color conversion</a>
	 */
	private static float hueToRGB(float p, float q, float t) {
		if (t < 0f) t += 1;
		if (t > 1f) t -= 1;
		if (t < 1f/6) return p + (q - p) * 6f * t;
		if (t < 1f/2) return q;
		if (t < 2f/3) return p + (q - p) * (2f/3 - t) * 6f;
		return p;
	}
	
	/**
	 * Decodes a <code>String</code> into a <code>Color</code>.
	 * Supports octal and hexadecimal number representations of opaque and transparent colors.
	 * @param input String that represents a color as a 24-bit or 32-bit integer
	 * @throws NumberFormatException If the string cannot be decoded.
	 * @return Color
	 * @deprecated Replaced by {@link #hex(String)} as of 2.1.0
	 */
	@Deprecated
	public static Color decode(String input) throws NumberFormatException {
		return hex(input);
	}
	
	public static Color hex(String hex) throws NumberFormatException {
		if (!hex.startsWith("#")) {
			hex = "#" + hex;
		}
		if (hex.length() == 4) {
			hex = hex.replaceAll("[a-zA-Z0-9]", "$0$0");
		}
		
		int i = Integer.decode(hex);
		
		if ((i >>> 24) != 0) {
			return new Color((i >> 24) & 0xFF, (i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
		} else {
			return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
		}
	}
	
	/**
	 * @deprecated Replaced by {@link ColorParser} as of 1.2.0
	 */
	@Deprecated
	public static Color parse(String input) {
		return new ColorParser().read(input);
	}
	
	public static Color white() {
		return new Color(255, 255, 255);
	}
	
	public static Color black() {
		return new Color(0, 0, 0);
	}
	
	public static Color red() {
		return new Color(255, 0, 0);
	}
	
	public static Color green() {
		return new Color(0, 128, 0);
	}
	
	public static Color blue() {
		return new Color(0, 0, 255);
	}
	
	public static Color transparent() {
		return new Color(1f, 1f, 1f, 0f);
	}
	
	public static Color aqua() {
		return new Color(0, 255, 255);
	}
	
	public static Color cyan() {
		return new Color(0, 255, 255);
	}
	
	public static Color darkBlue() {
		return new Color(0, 0, 139);
	}
	
	public static Color darkCyan() {
		return new Color(0, 139, 139);
	}
	
	public static Color darkGray() {
		return new Color(169, 169, 169);
	}
	
	public static Color darkGreen() {
		return new Color(0, 100, 0);
	}
	
	public static Color darkMagenta() {
		return new Color(139, 0, 139);
	}
	
	public static Color darkOrange() {
		return new Color(255, 140, 0);
	}
	
	public static Color darkRed() {
		return new Color(139, 0, 0);
	}
	
	public static Color dimGray() {
		return new Color(105, 105, 105);
	}
	
	public static Color gold() {
		return new Color(255, 215, 0);
	}
	
	public static Color gray() {
		return new Color(128, 128, 128);
	}
	
	public static Color lightBlue() {
		return new Color(173, 216, 230);
	}
	
	public static Color lightCyan() {
		return new Color(224, 255, 255);
	}
	
	public static Color lightGray() {
		return new Color(211, 211, 211);
	}
	
	public static Color lightGreen() {
		return new Color(144, 238, 144);
	}
	
	public static Color lightYellow() {
		return new Color(255, 255, 224);
	}
	
	public static Color lime() {
		return new Color(0, 255, 0);
	}
	
	public static Color magenta() {
		return new Color(255, 0, 255);
	}
	
	public static Color maroon() {
		return new Color(128, 0, 0);
	}
	
	public static Color mediumBlue() {
		return new Color(0, 0, 205);
	}
	
	public static Color mintCream() {
		return new Color(245, 255, 250);
	}
	
	public static Color navy() {
		return new Color(0, 0, 128);
	}
	
	public static Color olive() {
		return new Color(128, 128, 0);
	}
	
	public static Color orange() {
		return new Color(255, 165, 0);
	}
	
	public static Color orangeRed() {
		return new Color(255, 69, 0);
	}
	
	public static Color pink() {
		return new Color(255, 192, 203);
	}
	
	public static Color purple() {
		return new Color(128, 0, 128);
	}
	
	public static Color rebeccaPurple() {
		return new Color(102, 51, 153);
	}
	
	public static Color silver() {
		return new Color(192, 192, 192);
	}
	
	public static Color skyBlue() {
		return new Color(135, 206, 235);
	}
	
	public static Color springGreen() {
		return new Color(0, 255, 127);
	}
	
	public static Color teal() {
		return new Color(0, 128, 128);
	}
	
	public static Color tomato() {
		return new Color(255, 99, 71);
	}
	
	public static Color turquoise() {
		return new Color(64, 224, 208);
	}
	
	public static Color violet() {
		return new Color(238, 130, 238);
	}
	
	public static Color whiteSmoke() {
		return new Color(245, 245, 245);
	}
	
	public static Color yellow() {
		return new Color(255, 255, 0);
	}
	
}
