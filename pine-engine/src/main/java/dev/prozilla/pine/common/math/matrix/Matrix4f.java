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
import dev.prozilla.pine.common.math.vector.Vector4f;

import java.nio.FloatBuffer;

/**
 * Represents a 4x4-Matrix. GLSL equivalent to mat4.
 */
public class Matrix4f extends MatrixFloat<Vector4f, Matrix4f> {

    private float m00, m01, m02, m03;
    private float m10, m11, m12, m13;
    private float m20, m21, m22, m23;
    private float m30, m31, m32, m33;

    /**
     * Creates a 4x4 identity matrix.
     */
    public Matrix4f() {
        setIdentity();
    }

    /**
     * Creates a 4x4 matrix with specified columns.
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     * @param col3 Vector with values of the third column
     * @param col4 Vector with values of the fourth column
     */
    public Matrix4f(Vector4f col1, Vector4f col2, Vector4f col3, Vector4f col4) {
        m00 = col1.x;
        m10 = col1.y;
        m20 = col1.z;
        m30 = col1.w;

        m01 = col2.x;
        m11 = col2.y;
        m21 = col2.z;
        m31 = col2.w;

        m02 = col3.x;
        m12 = col3.y;
        m22 = col3.z;
        m32 = col3.w;

        m03 = col4.x;
        m13 = col4.y;
        m23 = col4.z;
        m33 = col4.w;
    }

    @Override
    public final Matrix4f setIdentity() {
        m00 = 1f;
        m11 = 1f;
        m22 = 1f;
        m33 = 1f;

        m01 = 0f;
        m02 = 0f;
        m03 = 0f;
        m10 = 0f;
        m12 = 0f;
        m13 = 0f;
        m20 = 0f;
        m21 = 0f;
        m23 = 0f;
        m30 = 0f;
        m31 = 0f;
        m32 = 0f;
        
        return this;
    }
    
    /**
     * Applies an orthographic projection to this matrix.
     * @param left Coordinate for the left vertical clipping pane
     * @param right Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top Coordinate for the top horizontal clipping pane
     * @param near Coordinate for the near depth clipping pane
     * @param far Coordinate for the far depth clipping pane
     * @return This matrix.
     */
    public Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        float rm00 = 2f / (right - left);
        float rm11 = 2f / (top - bottom);
        float rm22 = 2f / (near - far);
        float tx = (left + right) / (left - right);
        float ty = (top + bottom) / (bottom - top);
        float tz = (far + near) / (near - far);
        
        m03 = m00 * tx + m01 * ty + m02 * tz + m03;
        m13 = m10 * tx + m11 * ty + m12 * tz + m13;
        m23 = m20 * tx + m21 * ty + m22 * tz + m23;
        m33 = m30 * tx + m31 * ty + m32 * tz + m33;
        
        m00 *= rm00;
        m10 *= rm00;
        m20 *= rm00;
        m30 *= rm00;
        
        m01 *= rm11;
        m11 *= rm11;
        m21 *= rm11;
        m31 *= rm11;
        
        m02 *= rm22;
        m12 *= rm22;
        m22 *= rm22;
        m32 *= rm22;
        
        return this;
    }
    
    /**
     * Applies a perspective projection to this matrix.
     * @param fov Field of view angle in degrees
     * @param aspect The aspect ratio is the ratio of width to height
     * @param near Distance from the viewer to the near clipping plane, must be positive
     * @param far Distance from the viewer to the far clipping plane, must be positive
     * @return This matrix.
     */
    public Matrix4f perspective(float fov, float aspect, float near, float far) {
        float h = (float)Math.tan(Math.toRadians(fov) * 0.5);
        
        float rm00 = 1f / (h * aspect);
        float rm11 = 1f / h;
        float rm22;
        float rm32;
        boolean farInf = far > 0 && Float.isInfinite(far);
        boolean nearInf = near > 0 && Float.isInfinite(near);
        if (farInf) {
            float e = 1E-6f;
            rm22 = e - 1f;
            rm32 = (e - 2f) * near;
        } else if (nearInf) {
            float e = 1E-6f;
            rm22 = 1f - e;
            rm32 = (2f - e) * far;
        } else {
            rm22 = (far + near) / (near - far);
            rm32 = (far + far) * near / (near - far);
        }
        
        float n02 = m02 * rm22 - m03;
        float n12 = m12 * rm22 - m13;
        float n22 = m22 * rm22 - m23;
        float n32 = m32 * rm22 - m33;
        
        m03 = m02 * rm32;
        m13 = m12 * rm32;
        m23 = m22 * rm32;
        m33 = m32 * rm32;
        
        m00 *= rm00;
        m10 *= rm00;
        m20 *= rm00;
        m30 *= rm00;
        
        m01 *= rm11;
        m11 *= rm11;
        m21 *= rm11;
        m31 *= rm11;
        
        m02 = n02;
        m12 = n12;
        m22 = n22;
        m32 = n32;
        
        return this;
    }
    
    /**
     * Applies translation to this matrix.
     * @param translation The translation vector.
     * @return This matrix.
     */
    public Matrix4f translate(Vector3f translation) {
        return translate(translation.x, translation.y, translation.z);
    }
    
    /**
     * Applies translation to this matrix.
     * @param x x coordinate of translation vector
     * @param y y coordinate of translation vector
     * @param z z coordinate of translation vector
     * @return This matrix.
     */
    public Matrix4f translate(float x, float y, float z) {
        m03 = Math.fma(m00, x, Math.fma(m01, y, Math.fma(m02, z, m03)));
        m13 = Math.fma(m10, x, Math.fma(m11, y, Math.fma(m12, z, m13)));
        m23 = Math.fma(m20, x, Math.fma(m21, y, Math.fma(m22, z, m23)));
        m33 = Math.fma(m30, x, Math.fma(m31, y, Math.fma(m32, z, m33)));
        
        return this;
    }
    
    /**
     * Applies a rotation to this matrix.
     * @param rotation The rotation vector
     * @return This matrix.
     * @see #rotation(float, float, float)
     */
    public Matrix4f rotation(Vector3f rotation) {
        return rotation(rotation.x, rotation.y, rotation.z);
    }
    
    /**
     * Applies a combined rotation matrix from Euler angles (pitch around the x-axis, yaw around the y-axis, roll around the z-axis).
     * <p>
     * Rotation is applied to a vector in the order roll, then yaw, then pitch.
     * @param pitch Rotation around the x-axis in degrees
     * @param yaw Rotation around the y-axis in degrees
     * @param roll Rotation around the z-axis in degrees
     * @return This matrix.
     */
    public Matrix4f rotation(float pitch, float yaw, float roll) {
        return rotateX(pitch).rotateY(yaw).rotateZ(roll);
    }
    
    /**
     * Applies a rotation around the X-axis.
     * @param angle The angle of the rotation, in degrees
     * @return This matrix.
     */
    public Matrix4f rotateX(float angle) {
        float rad = (float)Math.toRadians(angle);
        float sin = (float)Math.sin(rad);
        float cos = (float)Math.cos(rad);
        
        float a = m01, b = m02;
        m01 = a * cos + b * sin;
        m02 = b * cos - a * sin;
        
        a = m11;
        b = m12;
        m11 = a * cos + b * sin;
        m12 = b * cos - a * sin;
        
        a = m21;
        b = m22;
        m21 = a * cos + b * sin;
        m22 = b * cos - a * sin;
        
        a = m31;
        b = m32;
        m31 = a * cos + b * sin;
        m32 = b * cos - a * sin;
        
        return this;
    }
    
    /**
     * Applies a rotation around the Y-axis.
     * @param angle The angle of the rotation, in degrees
     * @return This matrix.
     */
    public Matrix4f rotateY(float angle) {
        float rad = (float)Math.toRadians(angle);
        float sin = (float)Math.sin(rad);
        float cos = (float)Math.cos(rad);
        
        float a = m00, b = m02;
        m00 = a * cos - b * sin;
        m02 = a * sin + b * cos;
        
        a = m10;
        b = m12;
        m10 = a * cos - b * sin;
        m12 = a * sin + b * cos;
        
        a = m20;
        b = m22;
        m20 = a * cos - b * sin;
        m22 = a * sin + b * cos;
        
        a = m30;
        b = m32;
        m30 = a * cos - b * sin;
        m32 = a * sin + b * cos;
        
        return this;
    }
    
    /**
     * Applies a rotation around the Z-axis.
     * @param angle The angle of the rotation, in degrees
     * @return This matrix.
     */
    public Matrix4f rotateZ(float angle) {
        float rad = (float)Math.toRadians(angle);
        float sin = (float)Math.sin(rad);
        float cos = (float)Math.cos(rad);
        
        float a = m00, b = m01;
        m00 = a * cos + b * sin;
        m01 = b * cos - a * sin;
        
        a = m10;
        b = m11;
        m10 = a * cos + b * sin;
        m11 = b * cos - a * sin;
        
        a = m20;
        b = m21;
        m20 = a * cos + b * sin;
        m21 = b * cos - a * sin;
        
        a = m30;
        b = m31;
        m30 = a * cos + b * sin;
        m31 = b * cos - a * sin;
        
        return this;
    }
    
    /**
     * Applies a rotation to this matrix.
     * @param angle Angle of rotation, in degrees
     * @param rotation The rotation vector
     * @return This matrix.
     */
    public Matrix4f rotate(float angle, Vector3f rotation) {
        return rotate(angle, rotation.x, rotation.y, rotation.z);
    }
    
    /**
     * Applies a rotation to this matrix.
     * @param angle Angle of rotation in degrees
     * @param x x coordinate of the rotation vector
     * @param y y coordinate of the rotation vector
     * @param z z coordinate of the rotation vector
     * @return This matrix.
     */
    public Matrix4f rotate(float angle, float x, float y, float z) {
        float length = (float)Math.sqrt(x * x + y * y + z * z);
        if (length == 0f) {
            return this;
        }
        float invLength = 1f / length;
        x *= invLength;
        y *= invLength;
        z *= invLength;
        
        float rad = (float)Math.toRadians(angle);
        float cos = (float)Math.cos(rad);
        float sin = (float)Math.sin(rad);
        float omc = 1f - cos;
        
        float r00 = x * x * omc + cos;
        float r10 = y * x * omc + z * sin;
        float r20 = x * z * omc - y * sin;
        float r01 = x * y * omc - z * sin;
        float r11 = y * y * omc + cos;
        float r21 = y * z * omc + x * sin;
        float r02 = x * z * omc + y * sin;
        float r12 = y * z * omc - x * sin;
        float r22 = z * z * omc + cos;
        
        float a = m00, b = m01, c = m02;
        m00 = a * r00 + b * r10 + c * r20;
        m01 = a * r01 + b * r11 + c * r21;
        m02 = a * r02 + b * r12 + c * r22;
        
        a = m10;
        b = m11;
        c = m12;
        m10 = a * r00 + b * r10 + c * r20;
        m11 = a * r01 + b * r11 + c * r21;
        m12 = a * r02 + b * r12 + c * r22;
        
        a = m20;
        b = m21;
        c = m22;
        m20 = a * r00 + b * r10 + c * r20;
        m21 = a * r01 + b * r11 + c * r21;
        m22 = a * r02 + b * r12 + c * r22;
        
        a = m30;
        b = m31;
        c = m32;
        m30 = a * r00 + b * r10 + c * r20;
        m31 = a * r01 + b * r11 + c * r21;
        m32 = a * r02 + b * r12 + c * r22;
        
        return this;
    }
    
    /**
     * Applies uniform scaling to this matrix.
     * @param scale The scale.
     * @return This matrix.
     */
    public Matrix4f scale(float scale) {
        return scale(scale, scale, scale);
    }
    
    /**
     * Applies scaling to this matrix.
     * @param scale The scale.
     * @return This matrix.
     */
    public Matrix4f scale(Vector3f scale) {
        return scale(scale.x, scale.y, scale.z);
    }
    
    /**
     * Applies scaling to this matrix.
     * @param x Scale factor along the x coordinate
     * @param y Scale factor along the y coordinate
     * @param z Scale factor along the z coordinate
     * @return This matrix.
     */
    public Matrix4f scale(float x, float y, float z) {
        m00 *= x;
        m10 *= x;
        m20 *= x;
        m30 *= x;
        
        m01 *= y;
        m11 *= y;
        m21 *= y;
        m31 *= y;
        
        m02 *= z;
        m12 *= z;
        m22 *= z;
        m32 *= z;
        
        return this;
    }
    
    @Override
    public Matrix4f set(Matrix4f other) {
        m00 = other.m00;
        m10 = other.m10;
        m20 = other.m20;
        m30 = other.m30;
        
        m01 = other.m01;
        m11 = other.m11;
        m21 = other.m21;
        m31 = other.m31;
        
        m02 = other.m02;
        m12 = other.m12;
        m22 = other.m22;
        m32 = other.m32;
        
        m03 = other.m03;
        m13 = other.m13;
        m23 = other.m23;
        m33 = other.m33;
        
        return this;
    }
    
    @Override
    public Matrix4f add(Matrix4f other) {
        m00 += other.m00;
        m10 += other.m10;
        m20 += other.m20;
        m30 += other.m30;
        
        m01 += other.m01;
        m11 += other.m11;
        m21 += other.m21;
        m31 += other.m31;
        
        m02 += other.m02;
        m12 += other.m12;
        m22 += other.m22;
        m32 += other.m32;
        
        m03 += other.m03;
        m13 += other.m13;
        m23 += other.m23;
        m33 += other.m33;
        
        return this;
    }

    @Override
    public Matrix4f subtract(Matrix4f other) {
        m00 -= other.m00;
        m10 -= other.m10;
        m20 -= other.m20;
        m30 -= other.m30;
        
        m01 -= other.m01;
        m11 -= other.m11;
        m21 -= other.m21;
        m31 -= other.m31;
        
        m02 -= other.m02;
        m12 -= other.m12;
        m22 -= other.m22;
        m32 -= other.m32;
        
        m03 -= other.m03;
        m13 -= other.m13;
        m23 -= other.m23;
        m33 -= other.m33;
        
        return this;
    }

    @Override
    public Matrix4f multiply(float scalar) {
        m00 *= scalar;
        m10 *= scalar;
        m20 *= scalar;
        m30 *= scalar;
        
        m01 *= scalar;
        m11 *= scalar;
        m21 *= scalar;
        m31 *= scalar;
        
        m02 *= scalar;
        m12 *= scalar;
        m22 *= scalar;
        m32 *= scalar;
        
        m03 *= scalar;
        m13 *= scalar;
        m23 *= scalar;
        m33 *= scalar;
        
        return this;
    }

    @Override
    public Vector4f multiply(Vector4f vector) {
        float x = m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w;
        float y = m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w;
        float z = m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w;
        float w = m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w;
       
        return vector.set(x, y, z, w);
    }

    @Override
    public Matrix4f multiply(Matrix4f other) {
        float m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20 + this.m03 * other.m30;
        float m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20 + this.m13 * other.m30;
        float m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20 + this.m23 * other.m30;
        float m30 = this.m30 * other.m00 + this.m31 * other.m10 + this.m32 * other.m20 + this.m33 * other.m30;

        float m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21 + this.m03 * other.m31;
        float m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21 + this.m13 * other.m31;
        float m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21 + this.m23 * other.m31;
        float m31 = this.m30 * other.m01 + this.m31 * other.m11 + this.m32 * other.m21 + this.m33 * other.m31;

        float m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22 + this.m03 * other.m32;
        float m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22 + this.m13 * other.m32;
        float m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22 + this.m23 * other.m32;
        float m32 = this.m30 * other.m02 + this.m31 * other.m12 + this.m32 * other.m22 + this.m33 * other.m32;

        float m03 = this.m00 * other.m03 + this.m01 * other.m13 + this.m02 * other.m23 + this.m03 * other.m33;
        float m13 = this.m10 * other.m03 + this.m11 * other.m13 + this.m12 * other.m23 + this.m13 * other.m33;
        float m23 = this.m20 * other.m03 + this.m21 * other.m13 + this.m22 * other.m23 + this.m23 * other.m33;
        float m33 = this.m30 * other.m03 + this.m31 * other.m13 + this.m32 * other.m23 + this.m33 * other.m33;
        
        this.m00 = m00;
        this.m10 = m10;
        this.m20 = m20;
        this.m30 = m30;
        
        this.m01 = m01;
        this.m11 = m11;
        this.m21 = m21;
        this.m31 = m31;
        
        this.m02 = m02;
        this.m12 = m12;
        this.m22 = m22;
        this.m32 = m32;
        
        this.m03 = m03;
        this.m13 = m13;
        this.m23 = m23;
        this.m33 = m33;
        
        return this;
    }

    @Override
    public Matrix4f transpose() {
        float m01 = this.m01;
        float m10 = this.m10;
        float m02 = this.m02;
        float m20 = this.m20;
        float m03 = this.m03;
        float m30 = this.m30;
        float m12 = this.m12;
        float m21 = this.m21;
        float m13 = this.m13;
        float m31 = this.m31;
        float m23 = this.m23;
        float m32 = this.m32;
        
        this.m01 = m10;
        this.m10 = m01;
        this.m02 = m20;
        this.m20 = m02;
        this.m03 = m30;
        this.m30 = m03;
        this.m12 = m21;
        this.m21 = m12;
        this.m13 = m31;
        this.m31 = m13;
        this.m23 = m32;
        this.m32 = m23;
        
        return this;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00).put(m10).put(m20).put(m30);
        buffer.put(m01).put(m11).put(m21).put(m31);
        buffer.put(m02).put(m12).put(m22).put(m32);
        buffer.put(m03).put(m13).put(m23).put(m33);
        buffer.flip();
    }
    
    @Override
    public boolean equals(Matrix4f other) {
        return other != null
            && other.m00 == m00 && other.m01 == m01 && other.m02 == m02 && other.m03 == m03
            && other.m10 == m10 && other.m11 == m11 && other.m12 == m12 && other.m13 == m13
            && other.m20 == m20 && other.m21 == m21 && other.m22 == m22 && other.m23 == m23
            && other.m30 == m30 && other.m31 == m31 && other.m32 == m32 && other.m33 == m33;
    }
    
    @Override
    public Matrix4f clone() {
        return new Matrix4f().set(this);
    }
    
    @Override
    public Matrix4f self() {
        return this;
    }
    
}
