#version 410 core

in vec3 fPosition;
in vec3 fNormal;

out vec4 color;

void main() {
    color = vec4(fNormal, 1);
}
