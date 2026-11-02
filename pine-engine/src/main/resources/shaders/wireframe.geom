#version 410 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vec4 gColor[];
in vec2 gTexCoords[];
in float gTexId[];
in float gIsArrayTexture[];

out vec3 fBarycentric;
out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out float fIsArrayTexture;

void main() {
    for (int i = 0; i < 3; i++) {
        gl_Position = gl_in[i].gl_Position;
        fColor = gColor[i];
        fTexCoords = gTexCoords[i];
        fTexId = gTexId[i];
        fIsArrayTexture = gIsArrayTexture[i];
        fBarycentric = vec3(i == 0, i == 1, i == 2);
        EmitVertex();
    }
    EndPrimitive();
}