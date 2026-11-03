#version 410 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in vec4 gColor[];
in vec2 gTexCoords[];
flat in float gTexId[];
flat in int gTextureType[];

out vec3 fBarycentric;
out vec4 fColor;
out vec2 fTexCoords;
flat out float fTexId;
flat out int fTextureType;

void main() {
    for (int i = 0; i < 3; i++) {
        gl_Position = gl_in[i].gl_Position;
        fColor = gColor[i];
        fTexCoords = gTexCoords[i];
        fTexId = gTexId[i];
        fTextureType = gTextureType[i];
        fBarycentric = vec3(i == 0, i == 1, i == 2);
        EmitVertex();
    }
    EndPrimitive();
}