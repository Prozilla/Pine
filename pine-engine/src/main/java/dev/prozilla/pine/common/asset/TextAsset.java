package dev.prozilla.pine.common.asset;

import dev.prozilla.pine.common.system.ResourceUtils;

import java.io.InputStream;

public interface TextAsset extends Asset {
	
	default InputStream createInputStream() {
		if (getPath() == null) {
			return null;
		}
		return ResourceUtils.getResourceStream(getPath());
	}
	
}
