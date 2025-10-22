package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lwjgl.GLUtils;
import dev.prozilla.pine.common.util.EnumUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.GL_PIXEL_PACK_BUFFER;
import static org.lwjgl.opengl.GL21.GL_PIXEL_UNPACK_BUFFER;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL40.GL_DRAW_INDIRECT_BUFFER;
import static org.lwjgl.opengl.GL42.GL_ATOMIC_COUNTER_BUFFER;
import static org.lwjgl.opengl.GL43.GL_DISPATCH_INDIRECT_BUFFER;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL44.GL_QUERY_BUFFER;

/**
 * Represents a Vertex Buffer Object (VBO).
 */
public class VertexBufferObject implements Destructible {

    /**
     * Stores the handle of the VBO.
     */
    private final int id;

    /**
     * Creates a Vertex Buffer Object (VBO).
     */
    public VertexBufferObject() {
        id = glGenBuffers();
    }
	
	public void bind(Target target) {
		bind(target.getValue());
	}
	
    /**
     * Binds this VBO with specified target.
     * @param target Target to bind
     */
    public void bind(int target) {
        glBindBuffer(target, id);
    }
	
	public void uploadData(Target target, FloatBuffer data, Usage usage) {
		uploadData(target.getValue(), data, usage.getValue());
	}
	
    /**
     * Upload vertex data to this VBO with specified target, data and usage.
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, FloatBuffer data, int usage) {
        glBufferData(target, data, usage);
    }
	
	public void uploadData(Target target, long size, Usage usage) {
		uploadData(target.getValue(), size, usage.getValue());
	}
	
    /**
     * Upload null data to this VBO with specified target, size and usage.
     * @param target Target to upload
     * @param size   Size in bytes of the VBO data store
     * @param usage  Usage of the data
     */
    public void uploadData(int target, long size, int usage) {
        glBufferData(target, size, usage);
    }
	
	public void uploadSubData(Target target, long offset, FloatBuffer data) {
		uploadSubData(target.getValue(), offset, data);
	}
	
    /**
     * Upload sub data to this VBO with specified target, offset and data.
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    public void uploadSubData(int target, long offset, FloatBuffer data) {
        glBufferSubData(target, offset, data);
    }
	
	public void uploadData(Target target, IntBuffer data, Usage usage) {
		uploadData(target.getValue(), data, usage.getValue());
	}
	
    /**
     * Upload element data to this EBO with specified target, data and usage.
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    public void uploadData(int target, IntBuffer data, int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Deletes this VBO.
     */
    @Override
    public void destroy() {
        glDeleteBuffers(id);
    }

    /**
     * Getter for the Vertex Buffer Object ID.
     * @return Handle of the VBO
     */
    public int getId() {
        return id;
    }
	
	public boolean isBound() {
		return id == getBoundId();
	}
	
	public static int getBoundId() {
		return GLUtils.getInt(GL_VERTEX_ARRAY_BUFFER_BINDING);
	}
	
	public enum Target implements IntEnum {
		ARRAY_BUFFER(GL_ARRAY_BUFFER),
		ATOMIC_COUNTER_BUFFER(GL_ATOMIC_COUNTER_BUFFER),
		COPY_READ_BUFFER( GL_COPY_READ_BUFFER),
		COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER),
		DISPATCH_INDIRECT_BUFFER(GL_DISPATCH_INDIRECT_BUFFER),
		DRAW_INDIRECT_BUFFER(GL_DRAW_INDIRECT_BUFFER),
		ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER),
		PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER),
		PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER),
		QUERY_BUFFER(GL_QUERY_BUFFER),
		SHADER_STORAGE_BUFFER(GL_SHADER_STORAGE_BUFFER),
		TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
		TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER),
		UNIFORM_BUFFER(GL_UNIFORM_BUFFER);
		
		private final int value;
		
		Target(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}
	
	public enum Usage implements IntEnum {
		STREAM_DRAW(GL_STREAM_DRAW),
		STREAM_READ( GL_STREAM_READ),
		STREAM_COPY(  GL_STREAM_COPY),
		STATIC_DRAW(GL_STATIC_DRAW),
		STATIC_READ( GL_STATIC_READ),
		STATIC_COPY( GL_STATIC_COPY),
		DYNAMIC_DRAW( GL_DYNAMIC_DRAW),
		DYNAMIC_READ( GL_DYNAMIC_READ),
		DYNAMIC_COPY(  GL_DYNAMIC_COPY);
		
		private final int value;
		
		Usage(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}

}
