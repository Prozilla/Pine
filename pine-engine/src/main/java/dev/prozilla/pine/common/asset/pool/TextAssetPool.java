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
	
	/**
	 * Creates a new asset by reading and parsing a text file.
	 * @param path The path of the text file
	 * @return The new asset
	 */
	@Override
	protected T createAsset(String path) {
		StringBuilder stringBuilder = new StringBuilder();
		
		try (InputStream in = ResourceUtils.getResourceStream(path)) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			return fail(path, null, e);
		}
		
		return parse(path, stringBuilder.toString());
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
