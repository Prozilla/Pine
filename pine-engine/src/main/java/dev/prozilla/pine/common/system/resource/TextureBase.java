package dev.prozilla.pine.common.system.resource;

public interface TextureBase {
	
	void bind();
	
	void unbind();
	
	int getId();
	
	int getWidth();
	
	int getHeight();
	
	String getPath();
	
	void destroy();
	
	boolean isInArray();
	
}
