package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Abstract class for pools of assets that are created from text files.
 */
public abstract class TextAssetPool<T extends Asset> extends AssetPool<T> {
	
	public static final String PREPROCESS_ERROR = "Failed to preprocess";
	public static final String PARSE_ERROR = "Failed to parse";
	
	/**
	 * Creates a new asset by reading and parsing a text file.
	 * @param path The path of the text file
	 * @return The new asset
	 */
	@Override
	protected T createAsset(String path) {
		StringBuilder stringBuilder = new StringBuilder();
		
		try (InputStream in = createInputStream(path);
		     BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			return fail(path, e);
		}
		
		String content = preprocess(path, stringBuilder);
		if (content == null) {
			return fail(path, PREPROCESS_ERROR);
		}
		
		T result = parse(path, content);
		if (result == null) {
			return fail(path, PARSE_ERROR);
		}
		
		return result;
	}
	
	protected InputStream createInputStream(String path) {
		return ResourceUtils.getResourceStream(path);
	}
	
	/**
	 * Preprocesses the content of the text file.
	 * @param path The path of the text file
	 * @param stringBuilder The string builder containing the content of the text file.
	 * @return The preprocessed content of the text file, or {@code null} if preprocessing failed.
	 */
	protected String preprocess(String path, StringBuilder stringBuilder) {
		return stringBuilder.toString();
	}
	
	/**
	 * Parses the text file as an asset.
	 * @param path The path of the text file. Mostly used for debugging purposes.
	 * @param content The content of the text file
	 * @return The new asset
	 */
	protected abstract T parse(String path, String content);
	
	@Override
	protected String normalize(String path) {
		return PathUtils.addLeadingSlash(path);
	}
	
}