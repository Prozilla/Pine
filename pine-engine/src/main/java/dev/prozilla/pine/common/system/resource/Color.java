package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;

/**
 * Represents an RGBA color.
 */
public final class Color {

    public static final Color WHITE = new Color(1f, 1f, 1f);
    public static final Color BLACK = new Color(0f, 0f, 0f);
    public static final Color RED = new Color(1f, 0f, 0f);
    public static final Color GREEN = new Color(0f, 1f, 0f);
    public static final Color BLUE = new Color(0f, 0f, 1f);

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
     */
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
    
    public Color copyRGB(Color color) {
        setRGB(color.getRed(), color.getGreen(), color.getBlue());
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
     * Returns the color as a (x,y,z)-Vector.
     * @return The color as vec3.
     */
    public Vector3f toVector3f() {
        return new Vector3f(red, green, blue);
    }

    /**
     * Returns the color as a (x,y,z,w)-Vector.
     * @return The color as vec4.
     */
    public Vector4f toVector4f() {
        return new Vector4f(red, green, blue, alpha);
    }
    
    @Override
    public Color clone() {
	    return new Color(red, green, blue, alpha);
    }
    
    public void print() {
        System.out.printf("RGBA(%s, %s, %s, %s)%n", getRed(), getGreen(), getBlue(), getAlpha());
    }
    
    /**
     * Decodes a <code>String</code> into a <code>Color</code>.
     * Supports octal and hexadecimal number representations of opaque colors.
     * @param nm String that represents a color as a 24-bit integer
     * @throws NumberFormatException If the string cannot be decoded.
     * @return Color
     */
    public static Color decode(String nm) throws NumberFormatException {
	    int i = Integer.decode(nm);
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }
}
