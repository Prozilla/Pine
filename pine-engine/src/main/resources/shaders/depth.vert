#version 410 core

layout (location=0) in vec3 vPosition;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform bool uSnapPixels;
uniform vec2 uViewportSize;

out vec3 fPosition;

#include "common/snap-to-pixel.glsl"

void main() {
    mat4 mvp = uProjection * uView * uModel;
    vec4 position = mvp * vec4(vPosition, 1.0);

    if (uSnapPixels) {
        position = snapToPixel(position);
    }

    gl_Position = position;
    fPosition = position.xyz;
}
