#version 410 core

layout (location=0) in vec3 vPosition;
layout (location=1) in vec3 vNormal;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform bool uSnapPixels;
uniform vec2 uViewportSize;

out vec3 fNormal;

#include "common/snap-to-pixel.glsl"

void main() {
    // Calculate position
    mat4 modelView = uView * uModel;
    vec4 modelViewPosition =  modelView * vec4(vPosition, 1.0);
    vec4 position = uProjection * modelViewPosition;
    if (uSnapPixels) {
        position = snapToPixel(position);
    }
    gl_Position = position;

    // Calculate normal
    fNormal = (uModel * vec4(vNormal, 0.0)).xyz;
    if (length(fNormal) > 0) {
        fNormal = normalize(fNormal);
    }
}
