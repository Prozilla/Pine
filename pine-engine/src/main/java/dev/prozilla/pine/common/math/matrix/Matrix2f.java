package dev.prozilla.pine.common.math.matrix;

import dev.prozilla.pine.common.math.vector.Vector2f;

import java.nio.FloatBuffer;

/**
 * Represents a 2x2-Matrix. GLSL equivalent to mat2.
 */
public class Matrix2f extends MatrixFloat<Vector2f, Matrix2f> {

    private float m00, m01;
    private float m10, m11;

    /**
     * Creates a 2x2 identity matrix.
     */
    public Matrix2f() {
        setIdentity();
    }

    /**
     * Creates a 2x2 matrix with specified columns.
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     */
    public Matrix2f(Vector2f col1, Vector2f col2) {
        m00 = col1.x;
        m10 = col1.y;

        m01 = col2.x;
        m11 = col2.y;
    }

    @Override
    public final Matrix2f setIdentity() {
        m00 = 1f;
        m11 = 1f;
        
        m01 = 0f;
        m10 = 0f;
        
        return this;
    }
    
    @Override
    public Matrix2f set(Matrix2f other) {
        m00 = other.m00;
        m10 = other.m10;
        
        m01 = other.m01;
        m11 = other.m11;
        
        return this;
    }
    
    @Override
    public Matrix2f add(Matrix2f other) {
        m00 += other.m00;
        m10 += other.m10;
        
        m01 += other.m01;
        m11 += other.m11;
        
        return this;
    }
    
    @Override
    public Matrix2f subtract(Matrix2f other) {
        m00 -= other.m00;
        m10 -= other.m10;
        
        m01 -= other.m01;
        m11 -= other.m11;
        
        return this;
    }

    @Override
    public Matrix2f multiply(float scalar) {
        m00 *= scalar;
        m10 *= scalar;
        
        m01 *= scalar;
        m11 *= scalar;
        
        return this;
    }

    @Override
    public Vector2f multiply(Vector2f vector) {
        float x = m00 * vector.x + m01 * vector.y;
        float y = m10 * vector.x + m11 * vector.y;
        
        return vector.set(x, y);
    }

    @Override
    public Matrix2f multiply(Matrix2f other) {
        float m00 = this.m00 * other.m00 + this.m01 * other.m10;
        float m10 = this.m10 * other.m00 + this.m11 * other.m10;

        float m01 = this.m00 * other.m01 + this.m01 * other.m11;
        float m11 = this.m10 * other.m01 + this.m11 * other.m11;
        
        this.m00 = m00;
        this.m10 = m10;
        
        this.m01 = m01;
        this.m11 = m11;
        
        return this;
    }

    @Override
    public Matrix2f transpose() {
        float m01 = this.m01;
	    this.m01 = this.m10;
        this.m10 = m01;
        
        return this;
    }

    @Override
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00).put(m10);
        buffer.put(m01).put(m11);
        buffer.flip();
    }
    
    @Override
    public boolean equals(Matrix2f other) {
        return other != null
            && other.m00 == m00 && other.m01 == m01
            && other.m10 == m10 && other.m11 == m11;
    }
    
    @Override
    public Matrix2f clone() {
        return new Matrix2f().set(this);
    }
    
    @Override
    public Matrix2f self() {
        return this;
    }
    
}
