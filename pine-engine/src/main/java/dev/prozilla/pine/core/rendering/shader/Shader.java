package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.exception.GLException;
import dev.prozilla.pine.common.util.EnumUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

/**
 * Represents an OpenGL shader.
 */
public class Shader implements Asset {
	
	/**
	 * Stores the handle of the shader.
	 */
	private final int id;
	
	private String path;
	private int holderCount;
	
	private static final Pattern LOG_LINE_PATTERN = Pattern.compile(
		"\\s*(?:(?:ERROR|WARNING):\\s*\\d+:(\\d{1,9})|\\d+\\((\\d{1,9})\\)|\\d+:(\\d{1,9})\\(\\d+\\))",
		Pattern.CASE_INSENSITIVE
	);
	private static final Pattern LINE_BREAK = Pattern.compile("\\r\\n|\\n|\\r");
	/** Number of source lines shown before and after a line that caused an error. */
	private static final int ERROR_CONTEXT_LINES = 2;
	/** Maximum number of source lines that get an excerpt. The full info log is always included. */
	private static final int ERROR_MAX_LINES = 5;
	
	public Shader(Type type) {
		this(type.getValue());
	}
	
	/**
	 * Creates a shader with specified type.
	 * @param type Type of the shader
	 */
	public Shader(int type) {
		id = glCreateShader(type);
	}
	
	/**
	 * Sets the source code of this shader, compiles it and checks for errors.
	 */
	public void compile(String source) {
		glShaderSource(id, source);
		glCompileShader(id);
		checkCompileStatus(source);
	}
	
	/**
	 * Checks if the shader was compiled successfully.
	 * @throws GLException If the shader failed to compile.
	 */
	private void checkCompileStatus(String source) throws GLException {
		int status = glGetShaderi(id, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
			throw new GLException(formatCompileError(glGetShaderInfoLog(id), source));
		}
	}
	
	@Override
	public void hold() {
		holderCount++;
	}
	
	@Override
	public void drop() {
		holderCount--;
		checkHoldState();
	}
	
	@Override
	public int getHolderCount() {
		return holderCount;
	}
	
	/**
	 * Deletes the shader.
	 */
	@Override
	public void destroy() {
		AssetPools.shaders.remove(this);
		glDeleteShader(id);
	}
	
	/**
	 * Getter for the shader ID.
	 * @return Handle of this shader
	 */
	public int getId() {
		return id;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Creates a shader with specified type and source and compiles it.
	 * @param type Type of the shader
	 * @param source Source of the shader
	 * @return Compiled Shader from the specified source
	 */
	public static Shader createShader(Type type, String source) {
		return createShader(type.getValue(), source);
	}
	
	/**
	 * Creates a shader with specified type and source and compiles it.
	 * @param type Type of the shader
	 * @param source Source of the shader
	 * @return Compiled Shader from the specified source
	 */
	public static Shader createShader(int type, String source) {
		Shader shader = new Shader(type);
		shader.compile(source);
		
		return shader;
	}
	
	/**
	 * Combines a shader info log with the source lines it refers to.
	 * @param log The info log of the shader
	 * @param source The source that was compiled
	 * @return The info log, followed by an excerpt of the source around each line the log points to.
	 */
	private static String formatCompileError(String log, String source) {
		String result = log == null ? "" : log.trim();
		if (source == null || result.isEmpty()) {
			return result;
		}
		
		String[] sourceLines = LINE_BREAK.split(source, -1);
		StringBuilder excerpts = new StringBuilder();
		Set<Integer> reported = new HashSet<>();
		int omitted = 0;
		
		Matcher matcher = LOG_LINE_PATTERN.matcher("");
		for (String logLine : LINE_BREAK.split(result)) {
			matcher.reset(logLine);
			if (!matcher.lookingAt()) {
				continue;
			}
			
			String number = matcher.group(1) != null ? matcher.group(1)
				                : matcher.group(2) != null ? matcher.group(2)
					                  : matcher.group(3);
			int lineNumber = Integer.parseInt(number);
			
			if (!reported.add(lineNumber)) {
				continue;
			}
			if (reported.size() > ERROR_MAX_LINES) {
				omitted++;
				continue;
			}
			
			appendExcerpt(excerpts, sourceLines, lineNumber);
		}
		
		if (excerpts.isEmpty()) {
			return result;
		}
		
		StringBuilder message = new StringBuilder(result);
		message.append(excerpts);
		if (omitted > 0) {
			message.append("\n\n... and").append(omitted).append(" more line(s)");
		}
		return message.toString();
	}
	
	private static void appendExcerpt(StringBuilder out, String[] lines, int lineNumber) {
		if (lineNumber < 1 || lineNumber > lines.length) {
			return;
		}
		
		int start = Math.max(1, lineNumber - ERROR_CONTEXT_LINES);
		int end = Math.min(lines.length, lineNumber + ERROR_CONTEXT_LINES);
		int width = String.valueOf(end).length();
		
		for (int i = start; i <= end; i++) {
			out.append("\n")
				.append(i == lineNumber ? "> " : "  ")
				.append(String.format("%" + width + "d | ", i))
				.append(lines[i - 1].replace("\t", "    "));
		}
	}
	
	public static String define(String name, int value) {
		return String.format("#define %s %d%n", name, value);
	}
	
	public static String define(String name, String value) {
		return String.format("#define %s %s%n", name, value);
	}
	
	public static String constant(String name, String type, String expression) {
		return String.format("const %s %s = %s;", type, name, expression);
	}
	
	public enum Type implements IntEnum {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		COMPUTE( GL_COMPUTE_SHADER),
		TESS_CONTROL(GL_TESS_CONTROL_SHADER),
		TESS_EVALUATION(GL_TESS_EVALUATION_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER);
		
		private final int value;
		
		Type(int value) {
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
