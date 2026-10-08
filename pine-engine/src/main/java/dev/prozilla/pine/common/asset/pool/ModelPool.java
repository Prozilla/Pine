package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.model.Model;
import dev.prozilla.pine.common.property.selection.WrapMode;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.PathUtils;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.core.rendering.material.LitMaterial;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class ModelPool extends AssetPool<Model> {
	
	private int flags;
	private int defaultFlags;
	
	public static final int DEFAULT_FLAGS = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals;
	
	public ModelPool() {
		defaultFlags = DEFAULT_FLAGS;
		flags = defaultFlags;
	}
	
	public Model load(String path, int flags) {
		this.flags = flags;
		return load(path);
	}
	
	@Override
	public Model load(String path) {
		return super.load(path);
	}
	
	@Override
	protected Model createAsset(String path) {
		String filePath = ResourceUtils.getResourcePath(path);
		AIScene aiScene = aiImportFile(filePath, flags);
		
		if (aiScene == null) {
			return fail(path, aiGetErrorString());
		}
		
		try {
			PointerBuffer aiMaterials = aiScene.mMaterials();
			Material<?>[] materials = new Material[aiScene.mNumMaterials()];
			if (aiMaterials != null) {
				for (int i = 0; i < materials.length; i++) {
					AIMaterial aiMaterial;
					try {
						aiMaterial = AIMaterial.create(aiMaterials.get(i));
					} catch (Exception e) {
						return fail(path, "Invalid material", e);
					}
					
					materials[i] = createMaterial(aiMaterial, path);
				}
			}
			
			PointerBuffer aiMeshes = aiScene.mMeshes();
			
			if (aiMeshes == null) {
				return new Model();
			}
			
			StaticMesh[] meshes = new StaticMesh[aiScene.mNumMeshes()];
			Material<?>[] meshMaterials = new Material[meshes.length];
			for (int i = 0; i < meshes.length; i++) {
				AIMesh aiMesh;
				try {
					aiMesh = AIMesh.create(aiMeshes.get(i));
				} catch (Exception e) {
					return fail(path, "Invalid mesh", e);
				}
				
				meshes[i] = createMesh(aiMesh);
				meshMaterials[i] = WrapMode.CLIP.getElement(aiMesh.mMaterialIndex(), materials);
			}
			
			return new Model(meshes, meshMaterials);
		} catch (Exception e) {
			return fail(path, e);
		} finally {
			aiReleaseImport(aiScene);
		}
	}
	
	private StaticMesh createMesh(AIMesh aiMesh) {
		int totalVertices = aiMesh.mNumVertices();
		
		float[] vertices = new float[totalVertices * 3];
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		for (int j = 0; j < totalVertices; j++) {
			AIVector3D aiVertex = aiVertices.get(j);
			vertices[j * 3] = aiVertex.x();
			vertices[j * 3 + 1] = aiVertex.y();
			vertices[j * 3 + 2] = aiVertex.z();
		}
		
		int totalFaces = aiMesh.mNumFaces();
		AIFace.Buffer aiFaces = aiMesh.mFaces();
		
		int totalTriangles = 0;
		for (int j = 0; j < totalFaces; j++) {
			if (aiFaces.get(j).mNumIndices() == 3) {
				totalTriangles++;
			}
		}
		
		int[] triangles = new int[totalTriangles * 3];
		int offset = 0;
		for (int j = 0; j < totalFaces; j++) {
			AIFace aiFace = aiFaces.get(j);
			if (aiFace.mNumIndices() == 3) {
				IntBuffer indices = aiFace.mIndices();
				triangles[offset] = indices.get(0);
				triangles[offset + 1] = indices.get(1);
				triangles[offset + 2] = indices.get(2);
				offset += 3;
			}
		}
		
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
		int totalNormals = aiNormals != null ? aiNormals.remaining() : 0;
		float[] normals = new float[totalNormals * 3];
		for (int j = 0; j < totalNormals; j++) {
			AIVector3D aiNormal = aiNormals.get(j);
			normals[j * 3] = aiNormal.x();
			normals[j * 3 + 1] = aiNormal.y();
			normals[j * 3 + 2] = aiNormal.z();
		}
		
		float[] uvArray = new float[totalVertices * 2];
		AIVector3D.Buffer aiUVs = aiMesh.mTextureCoords(0);
		if (aiUVs != null) {
			for (int j = 0; j < totalVertices; j++) {
				AIVector3D aiUV = aiUVs.get(j);
				uvArray[j * 2] = aiUV.x();
				uvArray[j * 2 + 1] = aiUV.y();
			}
		}
		
		return new StaticMesh(vertices, triangles, normals, uvArray);
	}
	
	private Material<?> createMaterial(AIMaterial aiMaterial, String path) {
		LitMaterial material = new LitMaterial();
		AIColor4D color = AIColor4D.create();
		
		int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
		if (result == aiReturn_SUCCESS) {
			material.ambient = new Color(color.r(), color.g(), color.b(), color.a());
		}
		
		result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
		if (result == aiReturn_SUCCESS) {
			material.color = new Color(color.r(), color.g(), color.b(), color.a());
		}
		
		result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
		if (result == aiReturn_SUCCESS) {
			material.specular = new Color(color.r(), color.g(), color.b(), color.a());
		}
		
		float reflectance = 0f;
		float[] shininessFactor = new float[]{0f};
		int[] pMax = new int[]{1};
		result = aiGetMaterialFloatArray(aiMaterial, AI_MATKEY_SHININESS, aiTextureType_NONE, 0, shininessFactor, pMax);
		if (result == aiReturn_SUCCESS) {
			reflectance = shininessFactor[0];
		}
		material.reflectance = reflectance;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			AIString aiTexturePath = AIString.calloc(stack);
			aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, aiTexturePath, (IntBuffer)null,
				null, null, null, null, null);
		
			String texturePath = aiTexturePath.dataString();
			if (!texturePath.isEmpty()) {
				material.texture = AssetPools.textures.load(PathUtils.addTrailingSlash(PathUtils.getParent(path)) + new File(texturePath).getName());
				material.color = null;
			}
		}
		
		return material;
	}
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		flags = defaultFlags;
	}
	
	public void setDefaultFlags(int defaultFlags) {
		this.defaultFlags = defaultFlags;
	}
	
}
