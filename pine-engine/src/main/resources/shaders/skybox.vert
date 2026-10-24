#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec4 vColor;
layout (location=2) in vec3 vTexCoords;

out vec4 fColor;
out vec3 fTexCoords;

uniform mat4 uView;
uniform mat4 uProjection;

void main()
{
    fColor = vColor;
    fTexCoords = vTexCoords;

    mat4 rotation = mat4(mat3(uView));
    vec4 position = uProjection * rotation * vec4(vPosition, 1.0);
    gl_Position = position.xyww;
}