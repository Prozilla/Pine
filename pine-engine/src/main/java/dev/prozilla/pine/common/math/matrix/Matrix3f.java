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
package dev.prozilla.pine.common.math.matrix;

import dev.prozilla.pine.common.math.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Represents a 3x3-Matrix. GLSL equivalent to mat3.
 */
public class Matrix3f extends MatrixFloat<Vector3f, Matrix3f> {

    private float m00, m01, m02;
    private float m10, m11, m12;
    private float m20, m21, m22;

    /**
     * Creates a 3x3 identity matrix.
     */
    public Matrix3f() {
        setIdentity();
    }

    /**
     * Creates a 3x3 matrix with specified columns.
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     * @param col3 Vector with values of the third column
     */
    public Matrix3f(Vector3f col1, Vector3f col2, Vector3f col3) {
        m00 = col1.x;
        m10 = col1.y;
        m20 = col1.z;

        m01 = col2.x;
        m11 = col2.y;
        m21 = col2.z;

        m02 = col3.x;
        m12 = col3.y;
        m22 = col3.z;
    }

    @Override
    public final Matrix3f setIdentity() {
        m00 = 1f;
        m11 = 1f;
        m22 = 1f;

        m01 = 0f;
        m02 = 0f;
        m10 = 0f;
        m12 = 0f;
        m20 = 0f;
        m21 = 0f;
        
        return this;
    }
    
    @Override
    public Matrix3f set(Matrix3f other) {
        m00 = other.m00;
        m10 = other.m10;
        m20 = other.m20;
        
        m01 = other.m01;
        m11 = other.m11;
        m21 = other.m21;
        
        m02 = other.m02;
        m12 = other.m12;
        m22 = other.m22;
        
        return this;
    }
    
    @Override
    public Matrix3f add(Matrix3f other) {
        m00 += other.m00;
        m10 += other.m10;
        m20 += other.m20;

        m01 += other.m01;
        m11 += other.m11;
        m21 += other.m21;

        m02 += other.m02;
        m12 += other.m12;
        m22 += other.m22;

        return this;
    }
    
    @Override
    public Matrix3f subtract(Matrix3f other) {
        m00 -= other.m00;
        m10 -= other.m10;
        m20 -= other.m20;
        
        m01 -= other.m01;
        m11 -= other.m11;
        m21 -= other.m21;
        
        m02 -= other.m02;
        m12 -= other.m12;
        m22 -= other.m22;
        
        return this;
    }
    
    @Override
    public Matrix3f multiply(float scalar) {
        m00 *= scalar;
        m10 *= scalar;
        m20 *= scalar;
        
        m01 *= scalar;
        m11 *= scalar;
        m21 *= scalar;
        
        m02 *= scalar;
        m12 *= scalar;
        m22 *= scalar;

        return this;
    }

    @Override
    public Vector3f multiply(Vector3f vector) {
        float x = m00 * vector.x + m01 * vector.y + m02 * vector.z;
        float y = m10 * vector.x + m11 * vector.y + m12 * vector.z;
        float z = m20 * vector.x + m21 * vector.y + m22 * vector.z;
        
        return vector.set(x, y, z);
    }

    @Override
    public Matrix3f multiply(Matrix3f other) {
        float m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20;
        float m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20;
        float m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20;

        float m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21;
        float m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21;
        float m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21;

        float m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22;
        float m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22;
        float m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22;

        this.m00 = m00;
        this.m10 = m10;
        this.m20 = m20;
        
        this.m01 = m01;
        this.m11 = m11;
        this.m21 = m21;
        
        this.m02 = m02;
        this.m12 = m12;
        this.m22 = m22;
        
        return this;
    }

    @Override
    public Matrix3f transpose() {
        float m01 = this.m01;
        float m10 = this.m10;
        float m02 = this.m02;
        float m20 = this.m20;
        float m12 = this.m12;
        float m21 = this.m21;
        
        this.m01 = m10;
        this.m10 = m01;
        this.m02 = m20;
        this.m20 = m02;
        this.m12 = m21;
        this.m21 = m12;

        return this;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00).put(m10).put(m20);
        buffer.put(m01).put(m11).put(m21);
        buffer.put(m02).put(m12).put(m22);
        buffer.flip();
    }
    
    @Override
    public boolean equals(Matrix3f other) {
        return other != null
            && other.m00 == m00 && other.m01 == m01 && other.m02 == m02
            && other.m10 == m10 && other.m11 == m11 && other.m12 == m12
            && other.m20 == m20 && other.m21 == m21 && other.m22 == m22;
    }
    
    @Override
    public Matrix3f clone() {
        return new Matrix3f().set(this);
    }
    
    @Override
    public Matrix3f self() {
        return this;
    }
    
}
