package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.asset.model.Model;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.core.rendering.mesh.StaticMesh;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

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
			PointerBuffer aiMeshes = aiScene.mMeshes();
			
			if (aiMeshes == null) {
				return new Model();
			}
			
			StaticMesh[] meshes = new StaticMesh[aiScene.mNumMeshes()];
			for (int i = 0; i < meshes.length; i++) {
				AIMesh aiMesh;
				try {
					aiMesh = AIMesh.create(aiMeshes.get(i));
				} catch (Exception e) {
					return fail(path, "Invalid mesh", e);
				}
				
				meshes[i] = createMesh(aiMesh);
			}
			
			return new Model(meshes);
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
	
	@Override
	protected void prepareNext() {
		super.prepareNext();
		flags = defaultFlags;
	}
	
	public void setDefaultFlags(int defaultFlags) {
		this.defaultFlags = defaultFlags;
	}
	
}
