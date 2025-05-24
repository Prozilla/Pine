package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.property.style.CSSParser;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.PathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class StyleSheetPool extends AssetPool<StyleSheet> {
	
	@Override
	protected StyleSheet createAsset(String path) {
		StyleSheet styleSheet;
		try (InputStream inputStream = Objects.requireNonNull(AssetPools.class.getResourceAsStream(path));
		     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			
			while (line != null) {
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
				line = bufferedReader.readLine();
			}
			
			CSSParser parser = new CSSParser();
			if (!parser.parse(stringBuilder.toString())) {
				fail(path, "Error while parsing: " + parser.getError(), null);
				styleSheet = new StyleSheet();
			} else {
				styleSheet = parser.getResult();
			}
		} catch (IOException | NullPointerException e) {
			fail(path, "", e);
			styleSheet = new StyleSheet();
		}
		
		return styleSheet;
	}
	
	@Override
	protected String normalize(String path) {
		return PathUtils.addLeadingSlash(path);
	}
	
}
