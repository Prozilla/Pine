package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.exception.InvalidStringException;

import java.nio.FloatBuffer;

/**
 * 2-dimensional vector with floating point precision. GLSL equivalent to <code>vec2</code>.
 */
public class Vector2f extends VectorFloat<Vector2f> {

    public float x;
    public float y;
    
    /**
     * Creates a default 2-dimensional vector with all values set to <code>0f</code>.
     */
    public Vector2f() {
        this(0f, 0f);
    }
    
    /**
     * Creates a 2-dimensional vector with given values.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    @Override
    public Vector2f add(Vector2f vector2f) {
        x += vector2f.x;
        y += vector2f.y;
        return this;
    }
    
    @Override
    public Vector2f scale(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return x * x + y * y;
    }
    
    @Override
    public float dot(Vector2f vector2f) {
        return this.x * vector2f.x + this.y * vector2f.y;
    }
    
    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }
    
    @Override
    public boolean equals(Vector2f vector) {
        return vector.x == x && vector.y == y;
    }
    
    @Override
    public Vector2f clone() {
        return new Vector2f(x, y);
    }
    
    /**
     * Converts this vector to a string representation in the format "(x,y)".
     */
    @Override
    public String toString() {
        return String.format("(%s,%s)", x, y);
    }
    
    public static Vector2f parse(String input) throws InvalidStringException {
        Float[] floats = Vector.parseToFloats(input, 2);
        return new Vector2f(floats[0], floats[1]);
    }
}
