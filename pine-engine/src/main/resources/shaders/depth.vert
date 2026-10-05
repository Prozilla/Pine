#version 410 core

layout (location=0) in vec3 vPosition;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 fPosition;

void main() {
    mat4 mvp = uProjection * uView * uModel;
    vec4 position = mvp * vec4(vPosition, 1.0);
    gl_Position = position;
    fPosition = position.xyz;
}
