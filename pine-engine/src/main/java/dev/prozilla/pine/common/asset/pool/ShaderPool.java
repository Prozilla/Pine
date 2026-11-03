package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.rendering.shader.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ShaderPool extends TextAssetPool<Shader> {
	
	private int type;
	private final Map<String, String> macros;
	private final Map<String, String[]> constants;
	
	private static final Pattern INCLUDE_PATTERN = Pattern.compile(
		"[ \\t]*#[ \\t]*include[ \\t]+(?:\"([^\"]+)\"|<([^>]+)>)[ \\t]*(?://.*)?"
	);
	private static final Pattern INCLUDE_START_PATTERN = Pattern.compile("[ \\t]*#[ \\t]*include\\b.*");
	private static final Pattern VERSION_PATTERN = Pattern.compile("[ \\t]*#[ \\t]*version\\b.*");
	
	public ShaderPool() {
		macros = new LinkedHashMap<>();
		constants = new LinkedHashMap<>();
	}
	
	public Shader loadVertexShader(String path) {
		return load(Shader.Type.VERTEX, path);
	}
	
	public Shader loadFragmentShader(String path) {
		return load(Shader.Type.FRAGMENT, path);
	}
	
	public Shader loadComputeShader(String path) {
		return load(Shader.Type.COMPUTE, path);
	}
	
	public Shader loadGeometryShader(String path) {
		return load(Shader.Type.GEOMETRY, path);
	}
	
	public Shader load(Shader.Type type, String path) {
		return load(type.getValue(), path);
	}
	
	public Shader load(int type, String path) {
		this.type = type;
		return load(path);
	}
	
	@Override
	protected String preprocess(String path, StringBuilder stringBuilder) {
		String rootPath = normalizeIncludePath(path);
		if (rootPath == null) {
			error("Invalid path: " + path);
			return null;
		}
		
		// Expand includes first, so that the injected definitions apply to included code as well
		IncludeContext context = new IncludeContext(rootPath);
		StringBuilder expanded = new StringBuilder(stringBuilder.length());
		if (!expand(stringBuilder.toString(), rootPath, true, context, expanded)) {
			return null;
		}
		
		// Insert header after the version directive, or at the start if there is none
		StringBuilder header = createHeader();
		expanded.insert(Math.max(0, context.versionDirectiveEnd), header);
		
		return super.preprocess(path, expanded);
	}
	
	private StringBuilder createHeader() {
		StringBuilder header = new StringBuilder();
		
		for (Platform platform : Platform.values()) {
			header.append(Shader.define(platform.name(), platform.ordinal()));
		}
		header.append(Shader.define("PLATFORM", Platform.getOrdinal()));
		
		for (Map.Entry<String, String> definition : macros.entrySet()) {
			header.append(Shader.define(definition.getKey(), definition.getValue()));
		}
		
		for (Map.Entry<String, String[]> constant : constants.entrySet()) {
			header.append(Shader.constant(constant.getKey(), constant.getValue()[0], constant.getValue()[1]));
		}
		
		return header;
	}
	
	/**
	 * Appends the source of a file to {@code out}, replacing #include directives with the files they refer to.
	 * Directives inside block comments are left alone.
	 * @param source The source of the file
	 * @param sourcePath The normalized path of the file
	 * @param isRoot Whether this is the shader itself, as opposed to an included file
	 * @param context State shared by all files of the shader
	 * @param out The output
	 * @return {@code false} if an include failed, in which case {@code context.error} is set.
	 */
	private boolean expand(String source, String sourcePath, boolean isRoot, IncludeContext context, StringBuilder out) {
		context.stack.addLast(sourcePath);
		
		boolean inBlockComment = false;
		int position = 0;
		int length = source.length();
		
		while (position < length) {
			int lineEnd = source.indexOf('\n', position);
			if (lineEnd < 0) {
				lineEnd = length;
			}
			String line = source.substring(position, lineEnd);
			position = lineEnd + 1;
			
			if (!inBlockComment && VERSION_PATTERN.matcher(line).matches()) {
				// Only include version directive if in root shader file
				if (isRoot) {
					out.append(line).append('\n');
					if (context.versionDirectiveEnd < 0) {
						context.versionDirectiveEnd = out.length();
					}
				}
			} else if (!inBlockComment && INCLUDE_START_PATTERN.matcher(line).matches()) {
				if (!expandInclude(line, sourcePath, context, out)) {
					return false;
				}
			} else {
				out.append(line).append('\n');
			}
			
			inBlockComment = endsInBlockComment(line, inBlockComment);
		}
		
		context.stack.removeLast();
		return true;
	}
	
	/**
	 * Expands a single include directive.
	 * @return {@code false} if the include failed, in which case {@code context.error} is set.
	 */
	private boolean expandInclude(String line, String sourcePath, IncludeContext context, StringBuilder out) {
		Matcher matcher = INCLUDE_PATTERN.matcher(line);
		if (!matcher.matches()) {
			error(String.format("Invalid #include in %s: %s", sourcePath, line.trim()));
			return false;
		}
		
		String target = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
		String targetPath = resolveIncludePath(sourcePath, target);
		if (targetPath == null) {
			error(String.format("Invalid #include \"%s\" in %s", target, sourcePath));
			return false;
		}
		
		if (context.stack.contains(targetPath) || !context.included.add(targetPath)) {
			return true;
		}
		
		String content = readInclude(targetPath, target, sourcePath);
		if (content == null) {
			return false;
		}
		
		return expand(content, targetPath, false, context, out);
	}
	
	/**
	 * Reads the text of an included file.
	 * @return The text of the file, or {@code null} if it could not be read, in which case {@code context.error} is set.
	 */
	private String readInclude(String path, String target, String sourcePath) {
		StringBuilder content = new StringBuilder();
		
		try (InputStream in = createInputStream(path)) {
			if (in == null) {
				error(String.format("Failed to read #include \"%s\" in %s: resource not found: %s", target, sourcePath, path));
				return null;
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException | RuntimeException e) {
			error(String.format("Failed to read #include \"%s\" (resolved to \"%s\") in %s: %s", target, path, sourcePath, e.getMessage()));
			return null;
		}
		
		return content.toString();
	}
	
	/**
	 * Checks whether a block comment is still open at the end of a line.
	 * @param line The line
	 * @param inBlockComment Whether a block comment was open at the start of the line.
	 */
	private static boolean endsInBlockComment(String line, boolean inBlockComment) {
		int i = 0;
		int length = line.length();
		
		while (i < length) {
			if (inBlockComment) {
				int end = line.indexOf("*/", i);
				if (end < 0) {
					return true;
				}
				inBlockComment = false;
				i = end + 2;
			} else if (line.startsWith("//", i)) {
				return false;
			} else if (line.startsWith("/*", i)) {
				inBlockComment = true;
				i += 2;
			} else {
				i++;
			}
		}
		
		return inBlockComment;
	}
	
	private static String resolveIncludePath(String fromPath, String target) {
		return target.startsWith("/") ? normalizeIncludePath(target) : normalizeIncludePath(PathUtils.getParent(fromPath) + target);
	}
	
	private static String normalizeIncludePath(String path) {
		List<String> segments = new ArrayList<>();
		
		for (String segment : path.replace('\\', '/').split("/")) {
			if (segment.isEmpty() || segment.equals(".")) {
				continue;
			}
			if (segment.equals("..")) {
				if (segments.isEmpty()) {
					return null;
				}
				segments.removeLast();
			} else {
				segments.add(segment);
			}
		}
		
		return "/" + String.join("/", segments);
	}
	
	@Override
	protected Shader parse(String path, String content) {
		return Shader.createShader(type, content);
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		macros.clear();
		constants.clear();
	}
	
	public void addMacro(String name, String expression) {
		macros.put(name, expression);
	}
	
	public void addConstant(String type, String name, String expression) {
		constants.put(name, new String[]{ type, expression });
	}
	
	private static final class IncludeContext {
		
		/** Files that are currently being expanded (used to detect cycles). */
		public final Deque<String> stack = new ArrayDeque<>();
		/** Files that have already been included once. */
		public final Set<String> included = new HashSet<>();
		
		/** Position in the output right after the shader's own version directive, or {@code -1} if it has none. */
		public int versionDirectiveEnd = -1;
		
		public IncludeContext(String rootPath) {
			included.add(rootPath);
		}
		
	}
	
}