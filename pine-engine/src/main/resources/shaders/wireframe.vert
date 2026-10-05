#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec4 vColor;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 fBarycentric;
out vec4 fColor;

void main() {
    int corner = gl_VertexID % 3;
    fBarycentric = vec3(corner == 0, corner == 1, corner == 2);

    fColor = vColor;
    gl_Position = uProjection * uView * uModel * vec4(vPosition, 1.0);
}