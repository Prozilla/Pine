/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.exception.InvalidStringException;

import java.nio.FloatBuffer;

/**
 * 4-dimensional vector with floating point precision. GLSL equivalent to <code>vec4</code>.
 */
public class Vector4f extends VectorFloat<Vector4f> {
    
    public float x;
    public float y;
    public float z;
    public float w;
    
    /**
     * Creates a default 4-dimensional vector with all values set to <code>0f</code>.
     */
    public Vector4f() {
        this(0f, 0f, 0f, 0f);
    }
    
    /**
     * Creates a 4-dimensional vector with given values.
     */
    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4f add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }
    
    @Override
    public Vector4f add(Vector4f vector4f) {
        x += vector4f.x;
        y += vector4f.y;
        z += vector4f.z;
        w += vector4f.w;
        return this;
    }
    
    @Override
    public Vector4f scale(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }
    
    @Override
    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }
    
    @Override
    public float dot(Vector4f vector4f) {
        return x * vector4f.x + y * vector4f.y + z * vector4f.z + w * vector4f.w;
    }
    
    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z).put(w);
        buffer.flip();
    }
    
    @Override
    public boolean equals(Vector4f vector) {
        return vector.x == x && vector.y == y && vector.z == z && vector.w == w;
    }
    
    @Override
    public Vector4f clone() {
        return new Vector4f(x, y, z, w);
    }
    
    /**
     * Converts this vector to a string representation in the format "(x,y,z,w)".
     */
    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", x, y, z, w);
    }
    
    public static Vector4f parse(String input) throws InvalidStringException {
        Float[] floats = Vector.parseToFloats(input, 4);
        return new Vector4f(floats[0], floats[1], floats[2], floats[3]);
    }
}
