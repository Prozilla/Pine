package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.Transceivable;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;

/**
 * Represents an RGBA color.
 */
public final class Color implements Printable, Cloneable<Color>, Transceivable<Color> {

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
    
    @Override
    public void transmit(Color target) {
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
    public Color self() {
        return this;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Color color) ? equals(color) : super.equals(obj);
    }
    
    /**
     * Checks if two colors are equal.
     * Two colors are equal if they share the same R, G, B and A values.
     * @param color Color to compare with
     */
    @Override
    public boolean equals(Color color) {
        return color.getRed() == red && color.getGreen() == green && color.getBlue() == blue && color.getAlpha() == alpha;
    }
    
    @Override
    public Color clone() {
	    return new Color(red, green, blue, alpha);
    }
    
    @Override
    public String toString() {
        return String.format("rgba(%s, %s, %s, %s)", red, green, blue, alpha);
    }
    
    public static Color white() {
        return new Color(1f, 1f, 1f);
    }
    
    public static Color black() {
        return new Color(0f, 0f, 0f);
    }
    
    public static Color red() {
        return new Color(1f, 0f, 0f);
    }
    
    public static Color green() {
        return new Color(0f, 1f, 0f);
    }
    
    public static Color blue() {
        return new Color(0f, 0f, 1f);
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
