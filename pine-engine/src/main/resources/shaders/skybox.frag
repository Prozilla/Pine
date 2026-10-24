#version 410 core

in vec4 fColor;
in vec3 fTexCoords;

uniform samplerCube uCubemap;

out vec4 color;

void main() {
    color = fColor * texture(uCubemap, fTexCoords);
}
