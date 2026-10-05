#version 410 core

in vec3 fPosition;

uniform float uNear;
uniform float uFar;

out vec4 color;

void main() {
    float distanceToCamera = length(fPosition);
    float depth = pow(clamp((distanceToCamera - uNear) / (uFar - uNear), 0, 1), 0.25);
    color = vec4(vec3(1 - depth), 1);
}
